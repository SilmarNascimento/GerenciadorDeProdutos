package com.join.GerenciadorDeProdutos.service;

import com.join.GerenciadorDeProdutos.model.entity.Category;
import com.join.GerenciadorDeProdutos.model.entity.Product;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface CategoryServiceInterface {
  Page<Category> findAllCategories(int pageNumber, int pageSize, String query);
  Category findCategoryById(UUID productId);
  Category createCategory(Category product);
  Category updateCategoryById(UUID productId, Category product);
  void deleteCategory(UUID productId);
}
