package com.join.GerenciadorDeProdutos.controller;

import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.join.GerenciadorDeProdutos.controller.dto.category.CategoryInputDto;
import com.join.GerenciadorDeProdutos.exception.AlreadyExistsException;
import com.join.GerenciadorDeProdutos.exception.InvalidArgumentException;
import com.join.GerenciadorDeProdutos.exception.NotFoundException;
import com.join.GerenciadorDeProdutos.model.entity.Category;
import com.join.GerenciadorDeProdutos.service.CategoryServiceInterface;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CategoryControllerTest {
  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  private CategoryServiceInterface categoryService;

  private String baseUrl;
  private ObjectMapper objectMapper;
  private UUID mockCategoryId01;
  private UUID mockCategoryId02;
  private Category mockCategory01;
  private Category mockCategory02;
  private CategoryInputDto mockCategoryInputDto;
  private CategoryInputDto mockInvalidCategoryInputDto;

  @BeforeEach
  public void setUp() {
    baseUrl = "/api/v1/categories";
    mockCategoryId01 = UUID.randomUUID();
    mockCategoryId02 = UUID.randomUUID();
    objectMapper = new ObjectMapper();

    mockCategory01 = Category.builder()
        .id(mockCategoryId01)
        .name("Categoria A")
        .build();

    mockCategory02 = Category.builder()
        .id(mockCategoryId02)
        .name("Categoria B")
        .build();

    mockCategoryInputDto = new CategoryInputDto("Categoria a ser criada");
    mockInvalidCategoryInputDto = new CategoryInputDto("");
  }

  @Test
  @DisplayName("Verifica se é retornado uma lista paginada das entidades Category com default parameters")
  public void findAllCategoriesDefaultParametersTest() throws Exception {
    int pageNumber = 0;
    int pageSize = 20;
    long totalItems = 2;

    Page<Category> page = Mockito.mock(Page.class);

    Mockito
        .when(page.getNumberOfElements())
        .thenReturn(pageSize);
    Mockito
        .when(page.getTotalElements())
        .thenReturn(totalItems);
    Mockito
        .when(page.getTotalPages())
        .thenReturn(1);
    Mockito
        .when(page.getContent())
        .thenReturn(List.of(mockCategory01, mockCategory02));

    Mockito
        .when(categoryService.findAllCategories(anyInt(), anyInt(), any()))
        .thenReturn(page);

    ResultActions httpResponse = mockMvc.perform(get(baseUrl));

    httpResponse
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.pageItems").value(pageSize))
        .andExpect(jsonPath("$.totalItems").value(totalItems))
        .andExpect(jsonPath("$.pages").value(1))
        .andExpect(jsonPath("$.data", isA(List.class)))
        .andExpect(jsonPath("$.data.[0].id").value(mockCategoryId01.toString()))
        .andExpect(jsonPath("$.data.[0].name").value(mockCategory01.getName()))
        .andExpect(jsonPath("$.data.[1].id").value(mockCategoryId02.toString()))
        .andExpect(jsonPath("$.data.[1].name").value(mockCategory02.getName()));

    ArgumentCaptor<Integer> pageNumberCaptor = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> pageSizeCaptor = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);

    Mockito
        .verify(categoryService, Mockito.times(1))
        .findAllCategories(
            pageNumberCaptor.capture(),
            pageSizeCaptor.capture(),
            queryCaptor.capture()
        );

    assertEquals(pageNumber, pageNumberCaptor.getValue());
    assertEquals(pageSize, pageSizeCaptor.getValue());
    assertNull(queryCaptor.getValue());
  }

  @Test
  @DisplayName("Verifica se é retornado uma lista paginada das entidades Category com Path parameters")
  public void findAllCategoriesPathParametersTest() throws Exception {
    int pageNumber = 0;
    int pageSize = 2;
    long totalItems = 2;

    Page<Category> page = Mockito.mock(Page.class);

    Mockito
        .when(page.getNumberOfElements())
        .thenReturn(pageSize);
    Mockito
        .when(page.getTotalElements())
        .thenReturn(totalItems);
    Mockito
        .when(page.getTotalPages())
        .thenReturn(1);
    Mockito
        .when(page.getContent())
        .thenReturn(List.of(mockCategory01, mockCategory02));

    Mockito
        .when(categoryService.findAllCategories(anyInt(), anyInt(), any()))
        .thenReturn(page);

    String endpoint = baseUrl + "?pageNumber=" + pageNumber + "&pageSize=" + pageSize;
    ResultActions httpResponse = mockMvc.perform(get(endpoint));

    httpResponse
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.pageItems").value(pageSize))
        .andExpect(jsonPath("$.totalItems").value(totalItems))
        .andExpect(jsonPath("$.pages").value(1))
        .andExpect(jsonPath("$.data", isA(List.class)))
        .andExpect(jsonPath("$.data.[0].id").value(mockCategoryId01.toString()))
        .andExpect(jsonPath("$.data.[0].name").value(mockCategory01.getName()))
        .andExpect(jsonPath("$.data.[1].id").value(mockCategoryId02.toString()))
        .andExpect(jsonPath("$.data.[1].name").value(mockCategory02.getName()));

    ArgumentCaptor<Integer> pageNumberCaptor = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> pageSizeCaptor = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);

    Mockito
        .verify(categoryService, Mockito.times(1))
        .findAllCategories(
            pageNumberCaptor.capture(),
            pageSizeCaptor.capture(),
            queryCaptor.capture()
        );

    assertEquals(pageNumber, pageNumberCaptor.getValue());
    assertEquals(pageSize, pageSizeCaptor.getValue());
    assertNull(queryCaptor.getValue());
  }

  @Test
  @DisplayName("Verifica se é retornado uma lista paginada das entidades Category com filtro por query")
  public void findAllCategoriesQueryFilterTest() throws Exception {
    int pageNumber = 0;
    int pageSize = 20;
    long totalItems = 2;
    String query = "A";

    Page<Category> page = Mockito.mock(Page.class);

    Mockito
        .when(page.getNumberOfElements())
        .thenReturn(pageSize);
    Mockito
        .when(page.getTotalElements())
        .thenReturn(totalItems);
    Mockito
        .when(page.getTotalPages())
        .thenReturn(1);
    Mockito
        .when(page.getContent())
        .thenReturn(List.of(mockCategory01));

    Mockito
        .when(categoryService.findAllCategories(anyInt(), anyInt(), any(String.class)))
        .thenReturn(page);

    String endpoint = baseUrl + "?query=" + query;
    ResultActions httpResponse = mockMvc.perform(get(endpoint));

    httpResponse
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.pageItems").value(pageSize))
        .andExpect(jsonPath("$.totalItems").value(totalItems))
        .andExpect(jsonPath("$.pages").value(1))
        .andExpect(jsonPath("$.data", isA(List.class)))
        .andExpect(jsonPath("$.data.[0].id").value(mockCategoryId01.toString()))
        .andExpect(jsonPath("$.data.[0].name").value(mockCategory01.getName()));

    ArgumentCaptor<Integer> pageNumberCaptor = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> pageSizeCaptor = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);

    Mockito
        .verify(categoryService, Mockito.times(1))
        .findAllCategories(
            pageNumberCaptor.capture(),
            pageSizeCaptor.capture(),
            queryCaptor.capture()
        );

    assertEquals(pageNumber, pageNumberCaptor.getValue());
    assertEquals(pageSize, pageSizeCaptor.getValue());
    assertEquals(query, queryCaptor.getValue());
  }

  @Test
  @DisplayName("Verifica se é retornado uma entidade Category pelo seu id")
  public void findCategoriesByIdTest() throws Exception {
    Mockito
        .when(categoryService.findCategoryById(mockCategoryId01))
        .thenReturn(mockCategory01);

    String endpoint = baseUrl + "/" + mockCategoryId01.toString();
    ResultActions httpResponse = mockMvc.perform(get(endpoint));

    httpResponse
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.id").value(mockCategoryId01.toString()))
        .andExpect(jsonPath("$.name").value(mockCategory01.getName()));

    Mockito
        .verify(categoryService, Mockito.times(1))
        .findCategoryById(any(UUID.class));
  }

  @Test
  @DisplayName("Verifica se é disparado uma exceção quanto não se encontra uma entidade Category pelo seu id")
  public void findCategoryByIdTestNotFoundError() throws Exception {
    Mockito
        .when(categoryService.findCategoryById(mockCategoryId01))
        .thenThrow(new NotFoundException("Categoria não encontrada!"));

    String endpoint = baseUrl + "/" + mockCategoryId01.toString();
    ResultActions httpResponse = mockMvc.perform(get(endpoint));

    httpResponse
        .andExpect(status().is(404))
        .andExpect(jsonPath("$").value("Categoria não encontrada!"));

    Mockito
        .verify(categoryService, Mockito.times(1))
        .findCategoryById(any(UUID.class));
  }

  @Test
  @DisplayName("Verifica se é criado uma entidade Category")
  public void createCategoryTest() throws Exception {
    Mockito
        .when(categoryService.createCategory(any(Category.class)))
        .thenReturn(mockCategory01);

    String requestBody = objectMapper.writeValueAsString(mockCategoryInputDto);

    ResultActions httpResponse = mockMvc
        .perform(post(baseUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody));

    httpResponse
        .andExpect(status().is(201))
        .andExpect(jsonPath("$.id").value(mockCategoryId01.toString()))
        .andExpect(jsonPath("$.name").value(mockCategory01.getName()));

    Mockito
        .verify(categoryService, Mockito.times(1))
        .createCategory(any(Category.class));
  }

  @Test
  @DisplayName("Verifica se é disparado uma exceção quando se tenta cadastrar uma entidade Category já existente")
  public void createCategoryTestAlreadyExistsError() throws Exception {
    Mockito
        .when(categoryService.createCategory(any(Category.class)))
        .thenThrow(new AlreadyExistsException("Categoria já cadastrada!"));

    String requestBody = objectMapper.writeValueAsString(mockCategoryInputDto);

    ResultActions httpResponse = mockMvc
        .perform(post(baseUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody));

    httpResponse
        .andExpect(status().is(400))
        .andExpect(jsonPath("$").value("Categoria já cadastrada!"));

    Mockito
        .verify(categoryService, Mockito.times(1))
        .createCategory(any(Category.class));
  }

  @Test
  @DisplayName("Verifica se é disparado uma exceção quando se tenta cadastrar uma entidade Category com dados inválidos")
  public void createCategoryTestInvalidArgumentsError() throws Exception {
    Mockito
        .when(categoryService.createCategory(any(Category.class)))
        .thenThrow(new InvalidArgumentException("Nome da categoria não pode ser nulo ou vazio!"));

    String requestBody = objectMapper.writeValueAsString(mockInvalidCategoryInputDto);

    ResultActions httpResponse = mockMvc
        .perform(post(baseUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody));

    httpResponse
        .andExpect(status().is(400))
        .andExpect(jsonPath("$").value("Nome da categoria não pode ser nulo ou vazio!"));

    Mockito
        .verify(categoryService, Mockito.times(1))
        .createCategory(any(Category.class));
  }

  @Test
  @DisplayName("Verifica se uma entidade Category é atualizada")
  public void updateCategoryByIdTest() throws Exception {
    Mockito
        .when(categoryService.updateCategoryById(any(UUID.class), any(Category.class)))
        .thenReturn(mockCategory02);

    String requestBody = objectMapper.writeValueAsString(mockCategoryInputDto);
    String endpoint = baseUrl + "/" + mockCategoryId01.toString();

    ResultActions httpResponse = mockMvc
        .perform(put(endpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody));

    httpResponse
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.id").value(mockCategoryId02.toString()))
        .andExpect(jsonPath("$.name").value(mockCategory02.getName()));

    Mockito
        .verify(categoryService, Mockito.times(1))
        .updateCategoryById(any(UUID.class), any(Category.class));
  }

  @Test
  @DisplayName("Verifica se é disparado uma exceção quanto não se encontra uma entidade Category pelo seu id")
  public void updateCategoryByIdTestNotFoundTest() throws Exception {
    Mockito
        .when(categoryService.updateCategoryById(any(UUID.class), any(Category.class)))
        .thenThrow(new NotFoundException("Categoria não encontrada!"));

    String requestBody = objectMapper.writeValueAsString(mockCategoryInputDto);
    String endpoint = baseUrl + "/" + mockCategoryId01.toString();

    ResultActions httpResponse = mockMvc
        .perform(put(endpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody));

    httpResponse
        .andExpect(status().is(404))
        .andExpect(jsonPath("$").value("Categoria não encontrada!"));

    Mockito
        .verify(categoryService, Mockito.times(1))
        .updateCategoryById(any(UUID.class), any(Category.class));
  }

  @Test
  @DisplayName("Verifica se é disparado uma exceção quanto não se encontra uma entidade Category pelo seu id")
  public void updateCategoryByIdTestInvalidArgumentsTest() throws Exception {
    Mockito
        .when(categoryService.updateCategoryById(any(UUID.class), any(Category.class)))
        .thenThrow(new InvalidArgumentException("Nome da categoria não pode ser nulo ou vazio!"));

    String requestBody = objectMapper.writeValueAsString(mockCategoryInputDto);
    String endpoint = baseUrl + "/" + mockCategoryId01.toString();

    ResultActions httpResponse = mockMvc
        .perform(put(endpoint)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody));

    httpResponse
        .andExpect(status().is(400))
        .andExpect(jsonPath("$").value("Nome da categoria não pode ser nulo ou vazio!"));

    Mockito
        .verify(categoryService, Mockito.times(1))
        .updateCategoryById(any(UUID.class), any(Category.class));
  }

  @Test
  @DisplayName("Verifica se uma entidade Category é deletada")
  public void deleteCategoryTest() throws Exception {
    Mockito
        .doNothing()
        .when(categoryService).deleteCategoryById(any(UUID.class));

    String endpoint = baseUrl + "/" + mockCategoryId01.toString();

    ResultActions httpResponse = mockMvc.perform(delete(endpoint));

    httpResponse
        .andExpect(status().is(204));

    Mockito
        .verify(categoryService, Mockito.times(1))
        .deleteCategoryById(any(UUID.class));
  }

  @Test
  @DisplayName("Verifica se é disparado uma exceção quanto não se encontra uma entidade Category pelo seu id para deleção")
  public void deleteCategoryNotFoundErroTest() throws Exception {
    doThrow(new NotFoundException("Categoria não encontrada!"))
        .when(categoryService)
        .deleteCategoryById(any(UUID.class));

    String endpoint = baseUrl + "/" + mockCategoryId01.toString();

    ResultActions httpResponse = mockMvc.perform(delete(endpoint));

    httpResponse
        .andExpect(status().is(404))
        .andExpect(jsonPath("$").value("Categoria não encontrada!"));

    Mockito
        .verify(categoryService, Mockito.times(1))
        .deleteCategoryById(any(UUID.class));
  }
}
