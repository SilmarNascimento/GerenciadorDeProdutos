package com.join.GerenciadorDeProdutos.service.implementation;

import com.join.GerenciadorDeProdutos.exception.AlreadyExistsException;
import com.join.GerenciadorDeProdutos.exception.InvalidArgumentException;
import com.join.GerenciadorDeProdutos.exception.NotFoundException;
import com.join.GerenciadorDeProdutos.model.entity.Category;
import com.join.GerenciadorDeProdutos.model.entity.Product;
import com.join.GerenciadorDeProdutos.model.repository.CategoryRepository;
import com.join.GerenciadorDeProdutos.model.repository.ProductRepository;
import com.join.GerenciadorDeProdutos.service.ProductServiceInterface;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductServiceInterface {
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  @Override
  public Page<Product> findAllProducts(int pageNumber, int pageSize, String query) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    if (query == null || query.isBlank()) {
      return productRepository.findAllOrderByName(pageable, null);
    }
    return productRepository.findAllOrderByName(pageable, query.toLowerCase());
  }

  @Override
  public Product findProductById(UUID productId) {
    return productRepository.findById(productId)
        .orElseThrow(() -> new NotFoundException("Produto não encontrado!"));
  }

  @Override
  public Product createProduct(Product product, List<UUID> categoryIdList) {
    productRepository.findByName(product.getName().toLowerCase())
        .ifPresent(productFound -> {
          throw new AlreadyExistsException("Produto já cadastrado!");
        });

    validateInput(product);

    Product productToBeCreated = Product.builder()
        .name(product.getName().toLowerCase())
        .description(product.getDescription())
        .price(product.getPrice())
        .build();

    if (categoryIdList != null && !categoryIdList.isEmpty()) {
      List<Category> categoriesToAdd = categoryRepository.findAllById(categoryIdList);
      productToBeCreated.setCategories(categoriesToAdd);
    }

    return productRepository.save(productToBeCreated);
  }

  @Override
  public Product updateProductById(UUID productId, Product product, List<UUID> categoryIdList) {
    Product productFound = productRepository.findById(productId)
        .orElseThrow(() -> new NotFoundException("Produto não encontrado!"));

    validateInput(product);

    productFound.setName(product.getName().toLowerCase());
    productFound.setDescription(product.getDescription());
    productFound.setPrice(product.getPrice());

    List<Category> categoriesToAdd = categoryRepository.findAllById(categoryIdList);
    productFound.setCategories(categoriesToAdd);

    return productRepository.save(productFound);
  }

  @Override
  public void deleteProductById(UUID productId) {
    productRepository.findById(productId)
        .orElseThrow(() -> new NotFoundException("Produto não encontrado!"));

    productRepository.deleteById(productId);
  }

  private static void validateInput(Product product) {
    if (product.getName() == null || product.getName().isBlank()) {
      throw new InvalidArgumentException("Nome do produto não pode ser nulo ou vazio");
    }
    if (product.getPrice() == null || product.getPrice() < 0) {
      throw new InvalidArgumentException("Preço do produto não pode ser nulo ou negativo");
    }
  }
}
