package com.join.GerenciadorDeProdutos.service.implementation;

import com.join.GerenciadorDeProdutos.exception.AlreadyExistsException;
import com.join.GerenciadorDeProdutos.exception.NotFoundException;
import com.join.GerenciadorDeProdutos.model.entity.Category;
import com.join.GerenciadorDeProdutos.model.entity.Product;
import com.join.GerenciadorDeProdutos.model.repository.CategoryRepository;
import com.join.GerenciadorDeProdutos.model.repository.ProductRepository;
import com.join.GerenciadorDeProdutos.service.ProductServiceInterface;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    product.validate();

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

    product.validate();

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

  @Override
  public Product addCategories(UUID productId, List<UUID> categoriesId) {
    Product productFound = productRepository.findById(productId)
        .orElseThrow(() -> new NotFoundException("Produto não encontrado!"));

    List<Category> categoryListToAdd = categoryRepository.findAllById(categoriesId);
    if (categoryListToAdd.isEmpty()) {
      throw new NotFoundException("Nenhuma categoria encontrada com os IDs fornecidos!");
    }

    Set<Category> previousCategorySet = new HashSet<>(productFound.getCategories());
    previousCategorySet.addAll(categoryListToAdd);
    productFound.setCategories(new ArrayList<>(previousCategorySet));

    return productRepository.save(productFound);
  }

  @Override
  public Product removeCategories(UUID productId, List<UUID> categoriesId) {
    Product productFound = productRepository.findById(productId)
        .orElseThrow(() -> new NotFoundException("Produto não encontrado!"));

    productFound.getCategories().removeIf(category -> categoriesId.contains(category.getId()));

    return productRepository.save(productFound);
  }
}
