package com.join.GerenciadorDeProdutos.service;

import com.join.GerenciadorDeProdutos.model.entity.Product;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface ProductServiceInterface {
  Page<Product> findAllProducts(int pageNumber, int pageSize, String query);
  Product findProductById(UUID productId);
  Product createProduct(Product product);
  Product updateProductById(UUID productId, Product product);
  void deleteProductById(UUID productId);
  Product addCategories(UUID productId, List<UUID> categoriesId);
  Product removeCategories(UUID productId, List<UUID> categoriesId);
}
