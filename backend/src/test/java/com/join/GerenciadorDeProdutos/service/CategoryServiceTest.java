package com.join.GerenciadorDeProdutos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;

import com.join.GerenciadorDeProdutos.exception.AlreadyExistsException;
import com.join.GerenciadorDeProdutos.exception.InvalidArgumentException;
import com.join.GerenciadorDeProdutos.exception.NotFoundException;
import com.join.GerenciadorDeProdutos.model.entity.Category;
import com.join.GerenciadorDeProdutos.model.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

  @Autowired
  private CategoryServiceInterface categoryService;

  @MockitoBean
  private CategoryRepository categoryRepository;

  private UUID mockCategoryId01;
  private Category mockCategory01;
  private Category mockCategory02;

  @BeforeEach
  public void setUp() {
    mockCategoryId01 = UUID.randomUUID();
    UUID mockCategoryId02 = UUID.randomUUID();

    mockCategory01 = Category.builder()
        .id(mockCategoryId01)
        .name("Categoria a")
        .build();

    mockCategory02 = Category.builder()
        .id(mockCategoryId02)
        .name("Categoria b")
        .build();
  }

  @Test
  @DisplayName("Verifica se é retornado uma página com uma lista de todas as entidades Category")
  public void findAllCategoriesNullQueryTest() {
    int pageNumber = 0;
    int pageSize = 2;

    Pageable mockPageable = PageRequest.of(pageNumber, pageSize);
    Page<Category> page = Mockito.mock(Page.class);

    Mockito
        .when(page.getContent())
        .thenReturn(List.of(mockCategory01, mockCategory02));

    Mockito
        .when(categoryRepository.findAllOrderByName(eq(mockPageable), eq(null)))
        .thenReturn(page);

    Page<Category> serviceResponse = categoryService.findAllCategories(pageNumber, pageSize, null);
    List<String> categoriesName = serviceResponse.getContent().stream()
        .map(Category::getName)
        .toList();

    assertFalse(serviceResponse.isEmpty());
    assertInstanceOf(Page.class, serviceResponse);
    assertEquals(pageNumber, serviceResponse.getNumber());
    assertEquals(pageSize, serviceResponse.getContent().size());
    assertTrue(categoriesName.contains(mockCategory01.getName()));
    assertTrue(categoriesName.contains(mockCategory02.getName()));

    Mockito
        .verify(categoryRepository, Mockito.times(1))
        .findAllOrderByName(any(Pageable.class), eq(null));
  }

  @Test
  @DisplayName("Verifica se é retornado uma lista de todas as entidades Category por uma query")
  public void findAllCategoriesNonNullQueryTest() {
    int pageNumber = 0;
    int pageSize = 2;
    String query = "B";

    Pageable mockPageable = PageRequest.of(pageNumber, pageSize);
    Page<Category> page = Mockito.mock(Page.class);

    Mockito
        .when(page.getContent())
        .thenReturn(List.of(mockCategory02));

    Mockito
        .when(categoryRepository.findAllOrderByName(eq(mockPageable), eq(query.toLowerCase())))
        .thenReturn(page);

    Page<Category> serviceResponse = categoryService.findAllCategories(pageNumber, pageSize, query);
    List<String> categoriesName = serviceResponse.getContent().stream()
        .map(Category::getName)
        .toList();

    assertFalse(serviceResponse.isEmpty());
    assertInstanceOf(Page.class, serviceResponse);
    assertEquals(pageNumber, serviceResponse.getNumber());
    assertEquals(List.of(mockCategory01).size(), serviceResponse.getContent().size());
    assertFalse(categoriesName.contains(mockCategory01.getName()));
    assertTrue(categoriesName.contains(mockCategory02.getName()));

    Mockito
        .verify(categoryRepository, Mockito.times(1))
        .findAllOrderByName(any(Pageable.class), any(String.class));
  }

  @Test
  @DisplayName("Verifica se é retornado a entidade Category por seu Id")
  public void findCategoryByIdTest() {
    Mockito
        .when(categoryRepository.findById(any()))
        .thenReturn(Optional.of(mockCategory01));

    Category serviceResponse = categoryService.findCategoryById(mockCategoryId01);

    assertEquals(mockCategory01, serviceResponse);
    assertEquals(mockCategoryId01, serviceResponse.getId());
    assertEquals(mockCategory01.getName(), serviceResponse.getName());

    Mockito
        .verify(categoryRepository, Mockito.times(1))
        .findById(any(UUID.class));
  }

  @Test
  @DisplayName("Verifica se ocorre o disparo de uma exceção caso não se encontre uma entidade Category por seu Id")
  public void findCategoryByIdTestError() {
    Mockito
        .when(categoryRepository.findById(any()))
        .thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> categoryService.findCategoryById(mockCategoryId01));

    Mockito
        .verify(categoryRepository, Mockito.times(1))
        .findById(eq(mockCategoryId01));
  }

  @Test
  @DisplayName("Verifica se é criado uma a entidade Category")
  public void createCategoryTest() {
    Mockito
        .when(categoryRepository.findByName(any(String.class)))
        .thenReturn(Optional.empty());

    Mockito
        .when(categoryRepository.save(any(Category.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    Category createdCategory = categoryService.createCategory(mockCategory01);

    assertEquals(mockCategory01.getName().toLowerCase(), createdCategory.getName());

    Mockito
        .verify(categoryRepository, Mockito.times(1))
        .findByName(any(String.class));
    Mockito
        .verify(categoryRepository, Mockito.times(1))
        .save(any(Category.class));
  }

  @Test
  @DisplayName("Verifica se é disparado uma exceção ao tentar criar uma a entidade Category já existente")
  public void createCategoryAlreadyExistsTestError() {
    Mockito
        .when(categoryRepository.findByName(mockCategory01.getName().toLowerCase()))
        .thenReturn(Optional.of(mockCategory01));

    assertThrows(AlreadyExistsException.class, () -> {
      categoryService.createCategory(mockCategory01);
    });

    Mockito
        .verify(categoryRepository, Mockito.times(1))
        .findByName(any(String.class));
  }

  @Test
  @DisplayName("Verifica se é disparado uma exceção ao tentar criar uma a entidade Category com argumentos inválidos")
  public void createCategoryInvalidArgumentsTestError() {
    Category invalidCategory = Category
        .builder()
        .name("")
        .build();

    Mockito
        .when(categoryRepository.findByName(any(String.class)))
        .thenReturn(Optional.empty());

    assertThrows(InvalidArgumentException.class, () -> {
      categoryService.createCategory(invalidCategory);
    });

    Mockito
        .verify(categoryRepository, Mockito.times(1))
        .findByName(any(String.class));
  }

  @Test
  @DisplayName("Verifica se é atualizado a entidade Category com sucesso")
  public void updateCategoryByIdTest() {
    Mockito
        .when(categoryRepository.findById(mockCategoryId01))
        .thenReturn(Optional.of(mockCategory01));

    Mockito
        .when(categoryRepository.save(any(Category.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    Category serviceResponse = categoryService.updateCategoryById(mockCategoryId01, mockCategory02);

    assertNotNull(serviceResponse.getId());
    assertEquals(mockCategoryId01, serviceResponse.getId());
    assertEquals(mockCategory02.getName().toLowerCase(), serviceResponse.getName());

    Mockito
        .verify(categoryRepository, Mockito.times(1))
        .findById(mockCategoryId01);
    Mockito
        .verify(categoryRepository, Mockito.times(1))
        .save(any(Category.class));
  }

  @Test
  @DisplayName("Verifica se lança exceção quando a Category não é encontrado para atualização")
  public void updateProductByIdNotFoundTest() {
    Mockito
        .when(categoryRepository.findById(mockCategoryId01))
        .thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> {
      categoryService.updateCategoryById(mockCategoryId01, mockCategory02);
    });

    Mockito
        .verify(categoryRepository, Mockito.times(1))
        .findById(mockCategoryId01);
  }

  @Test
  @DisplayName("Verifica se é disparado uma exceção ao tentar atualizar uma a entidade Category com argumentos inválidos")
  public void updateCategoryByIdInvalidArgumentsTest() {
    Category invalidCategory = Category
        .builder()
        .name("")
        .build();

    Mockito
        .when(categoryRepository.findById(mockCategoryId01))
        .thenReturn(Optional.of(mockCategory01));

    assertThrows(InvalidArgumentException.class, () -> {
      categoryService.updateCategoryById(mockCategoryId01, invalidCategory);
    });

    Mockito
        .verify(categoryRepository, Mockito.times(1))
        .findById(mockCategoryId01);
  }

  @Test
  @DisplayName("Verifica se exclui a entidade Category com sucesso")
  public void deleteCategoryByIdTest() {
    Mockito
        .when(categoryRepository.findById(mockCategoryId01))
        .thenReturn(Optional.of(mockCategory01));

    doNothing()
        .when(categoryRepository)
        .deleteById(mockCategoryId01);

    categoryService.deleteCategoryById(mockCategoryId01);

    Mockito
        .verify(categoryRepository, Mockito.times(1))
        .findById(mockCategoryId01);
    Mockito
        .verify(categoryRepository, Mockito.times(1))
        .deleteById(mockCategoryId01);
  }

  @Test
  @DisplayName("Verifica se lança exceção quando a Category não é encontrada para exclusão")
  public void deleteCategoryByIdNotFoundTest() {
    Mockito
        .when(categoryRepository.findById(mockCategoryId01))
        .thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> categoryService.deleteCategoryById(mockCategoryId01));

    Mockito
        .verify(categoryRepository, Mockito.times(1))
        .findById(mockCategoryId01);
    Mockito
        .verify(categoryRepository, Mockito.never())
        .deleteById(any(UUID.class));
  }
}
