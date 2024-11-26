package com.join.GerenciadorDeProdutos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;

import com.join.GerenciadorDeProdutos.exception.AlreadyExistsException;
import com.join.GerenciadorDeProdutos.exception.InvalidArgumentException;
import com.join.GerenciadorDeProdutos.exception.NotFoundException;
import com.join.GerenciadorDeProdutos.model.entity.Category;
import com.join.GerenciadorDeProdutos.model.entity.Product;
import com.join.GerenciadorDeProdutos.model.repository.CategoryRepository;
import com.join.GerenciadorDeProdutos.model.repository.ProductRepository;
import java.util.ArrayList;
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
public class ProductServiceTest {

  @Autowired
  private ProductServiceInterface productService;

  @MockitoBean
  private ProductRepository productRepository;

  @MockitoBean
  private CategoryRepository categoryRepository;

  private UUID mockProductId01;
  private UUID mockProductId02;
  private UUID mockCategoryId01;
  private UUID mockCategoryId02;
  private Product mockProduct01;
  private Product mockProduct02;
  private Category mockCategory01;
  private Category mockCategory02;

  @BeforeEach
  public void setUp() {
    mockProductId01 = UUID.randomUUID();
    mockProductId02 = UUID.randomUUID();
    mockCategoryId01 = UUID.randomUUID();
    mockCategoryId02 = UUID.randomUUID();

    mockProduct01 = Product.builder()
        .id(mockProductId01)
        .name("Produto a")
        .description("Descrição a")
        .price(10.0)
        .build();

    mockProduct02 = Product.builder()
        .id(mockProductId02)
        .name("Produto b")
        .description("Descrição b")
        .price(20.0)
        .build();

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
  @DisplayName("Verifica se é retornado uma página com uma lista de todas as entidades Product")
  public void findAllProductsNullQueryTest() {
    int pageNumber = 0;
    int pageSize = 2;

    Pageable mockPageable = PageRequest.of(pageNumber, pageSize);
    Page<Product> page = Mockito.mock(Page.class);

    Mockito
        .when(page.getContent())
        .thenReturn(List.of(mockProduct01, mockProduct02));

    Mockito
        .when(productRepository.findAllOrderByName(eq(mockPageable), eq(null)))
        .thenReturn(page);

    Page<Product> serviceResponse = productService.findAllProducts(pageNumber, pageSize, null);
    List<String> productsName = serviceResponse.getContent().stream()
        .map(Product::getName)
        .toList();

    assertFalse(serviceResponse.isEmpty());
    assertInstanceOf(Page.class, serviceResponse);
    assertEquals(pageNumber, serviceResponse.getNumber());
    assertEquals(pageSize, serviceResponse.getContent().size());
    assertTrue(productsName.contains(mockProduct01.getName()));
    assertTrue(productsName.contains(mockProduct02.getName()));

    Mockito
        .verify(productRepository, Mockito.times(1))
        .findAllOrderByName(any(Pageable.class), eq(null));
  }

  @Test
  @DisplayName("Verifica se é retornado uma lista de todas as entidades Product por uma query")
  public void findAllProductsNonNullQueryTest() {
    int pageNumber = 0;
    int pageSize = 2;
    String query = "B";

    Pageable mockPageable = PageRequest.of(pageNumber, pageSize);
    Page<Product> page = Mockito.mock(Page.class);

    Mockito
        .when(page.getContent())
        .thenReturn(List.of(mockProduct02));

    Mockito
        .when(productRepository.findAllOrderByName(eq(mockPageable), eq(query.toLowerCase())))
        .thenReturn(page);

    Page<Product> serviceResponse = productService.findAllProducts(pageNumber, pageSize, query);
    List<String> productsName = serviceResponse.getContent().stream()
        .map(Product::getName)
        .toList();

    assertFalse(serviceResponse.isEmpty());
    assertInstanceOf(Page.class, serviceResponse);
    assertEquals(pageNumber, serviceResponse.getNumber());
    assertEquals(List.of(mockProduct02).size(), serviceResponse.getContent().size());
    assertFalse(productsName.contains(mockProduct01.getName()));
    assertTrue(productsName.contains(mockProduct02.getName()));

    Mockito
        .verify(productRepository, Mockito.times(1))
        .findAllOrderByName(any(Pageable.class), any(String.class));
  }

  @Test
  @DisplayName("Verifica se é retornado a entidade Product por seu Id")
  public void findProductByIdTest() {
    Mockito
        .when(productRepository.findById(any()))
        .thenReturn(Optional.of(mockProduct01));

    Product serviceResponse = productService.findProductById(mockProductId01);

    assertEquals(mockProduct01, serviceResponse);
    assertEquals(mockProductId01, serviceResponse.getId());
    assertEquals(mockProduct01.getName(), serviceResponse.getName());
    assertEquals(mockProduct01.getDescription(), serviceResponse.getDescription());
    assertEquals(mockProduct01.getPrice(), serviceResponse.getPrice());

    Mockito
        .verify(productRepository, Mockito.times(1))
        .findById(any(UUID.class));
  }

  @Test
  @DisplayName("Verifica se ocorre o disparo de uma exceção caso não se encontre uma entidade Product por seu Id")
  public void findProductByIdTestError() {
    Mockito
        .when(productRepository.findById(any()))
        .thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> productService.findProductById(mockProductId01));

    Mockito
        .verify(productRepository, Mockito.times(1))
        .findById(eq(mockProductId01));
  }

  @Test
  @DisplayName("Verifica se é criado uma a entidade Product")
  public void createProductTest() {
    Mockito
        .when(productRepository.findByName(any(String.class)))
        .thenReturn(Optional.empty());

    Mockito
        .when(productRepository.save(any(Product.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    Product createdProduct = productService.createProduct(mockProduct01, new ArrayList<>());

    assertEquals(mockProduct01.getName().toLowerCase(), createdProduct.getName());
    assertEquals(mockProduct01.getDescription(), createdProduct.getDescription());
    assertEquals(mockProduct01.getPrice(), createdProduct.getPrice());

    Mockito
        .verify(productRepository, Mockito.times(1))
        .findByName(any(String.class));
    Mockito
        .verify(productRepository, Mockito.times(1))
        .save(any(Product.class));
  }

  @Test
  @DisplayName("Verifica se é criado uma a entidade Product com uma lista de Categorias")
  public void createProductCategoryArrayTest() {
    Mockito
        .when(productRepository.findByName(any(String.class)))
        .thenReturn(Optional.empty());

    Mockito
        .when(categoryRepository.findAllById(any(List.class)))
        .thenReturn(List.of(mockCategory01, mockCategory02));

    Mockito
        .when(productRepository.save(any(Product.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    Product createdProduct = productService.createProduct(mockProduct01, List.of(mockCategoryId01, mockCategoryId02));

    assertEquals(mockProduct01.getName().toLowerCase(), createdProduct.getName());
    assertEquals(mockProduct01.getDescription(), createdProduct.getDescription());
    assertEquals(mockProduct01.getPrice(), createdProduct.getPrice());
    assertTrue(createdProduct.getCategories().contains(mockCategory01));
    assertTrue(createdProduct.getCategories().contains(mockCategory02));

    Mockito
        .verify(productRepository, Mockito.times(1))
        .findByName(any(String.class));
    Mockito
        .verify(productRepository, Mockito.times(1))
        .save(any(Product.class));
  }

  @Test
  @DisplayName("Verifica se é disparado uma exceção ao tentar criar uma a entidade Product já existente")
  public void createProductAlreadyExistsTestError() {
    Mockito
        .when(productRepository.findByName(mockProduct01.getName().toLowerCase()))
        .thenReturn(Optional.of(mockProduct01));

    assertThrows(AlreadyExistsException.class, () -> {
      productService.createProduct(mockProduct01, new ArrayList<>());
    });

    Mockito
        .verify(productRepository, Mockito.times(1))
        .findByName(any(String.class));
  }

  @Test
  @DisplayName("Verifica se é disparado uma exceção ao tentar criar uma a entidade Product com argumentos inválidos")
  public void createProductInvalidArgumentsTestError() {
    Product invalidProduct = Product
        .builder()
        .name("")
        .build();

    Mockito
        .when(productRepository.findByName(any(String.class)))
        .thenReturn(Optional.empty());

    assertThrows(InvalidArgumentException.class, () -> {
      productService.createProduct(invalidProduct, new ArrayList<>());
    });

    Mockito
        .verify(productRepository, Mockito.times(1))
        .findByName(any(String.class));
  }

  @Test
  @DisplayName("Verifica se é atualizado a entidade Product com sucesso")
  public void updateProductByIdTest() {
    List<UUID> categoryIdList = new ArrayList<>();

    Mockito
        .when(productRepository.findById(mockProductId01))
        .thenReturn(Optional.of(mockProduct01));

    Mockito
        .when(categoryRepository.findAllById(categoryIdList))
        .thenReturn(new ArrayList<>());

    Mockito
        .when(productRepository.save(any(Product.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    Product serviceResponse = productService.updateProductById(mockProductId01, mockProduct02, categoryIdList);

    assertNotNull(serviceResponse.getId());
    assertEquals(mockProductId01, serviceResponse.getId());
    assertEquals(mockProduct02.getName().toLowerCase(), serviceResponse.getName());
    assertEquals(mockProduct02.getDescription(), serviceResponse.getDescription());
    assertEquals(mockProduct02.getPrice(), serviceResponse.getPrice());

    Mockito
        .verify(productRepository, Mockito.times(1))
        .findById(mockProductId01);
    Mockito
        .verify(categoryRepository, Mockito.times(1))
        .findAllById(categoryIdList);
    Mockito
        .verify(productRepository, Mockito.times(1))
        .save(any(Product.class));
  }

  @Test
  @DisplayName("Verifica se lança exceção quando o Product não é encontrado para atualização")
  public void updateProductByIdNotFoundTest() {
    Mockito
        .when(productRepository.findById(mockProductId01))
        .thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> {
      productService.updateProductById(mockProductId01, mockProduct02, new ArrayList<>());
    });

    Mockito
        .verify(productRepository, Mockito.times(1))
        .findById(mockProductId01);
  }

  @Test
  @DisplayName("Verifica se é disparado uma exceção ao tentar atualizar uma a entidade Product com argumentos inválidos")
  public void updateProductByIdInvalidArgumentsTest() {
    Product invalidProduct = Product
        .builder()
        .name("")
        .build();

    Mockito
        .when(productRepository.findById(mockProductId01))
        .thenReturn(Optional.of(mockProduct01));

    assertThrows(InvalidArgumentException.class, () -> {
      productService.updateProductById(mockProductId01, invalidProduct, new ArrayList<>());
    });

    Mockito
        .verify(productRepository, Mockito.times(1))
        .findById(mockProductId01);
  }

  @Test
  @DisplayName("Verifica se exclui a entidade Product com sucesso")
  public void deleteProductByIdTest() {
    Mockito
        .when(productRepository.findById(mockProductId01))
        .thenReturn(Optional.of(mockProduct01));

    doNothing()
        .when(productRepository)
        .deleteById(mockProductId01);

    productService.deleteProductById(mockProductId01);

    Mockito
        .verify(productRepository, Mockito.times(1))
        .findById(mockProductId01);
    Mockito
        .verify(productRepository, Mockito.times(1))
        .deleteById(mockProductId01);
  }

  @Test
  @DisplayName("Verifica se lança exceção quando o Product não é encontrado para exclusão")
  public void deleteProductByIdNotFoundTest() {
    Mockito
        .when(productRepository.findById(mockProductId01))
        .thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> productService.deleteProductById(mockProductId01));

    Mockito
        .verify(productRepository, Mockito.times(1))
        .findById(mockProductId01);
    Mockito
        .verify(productRepository, Mockito.never())
        .deleteById(any(UUID.class));
  }
}

