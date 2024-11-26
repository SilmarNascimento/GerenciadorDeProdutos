package com.join.GerenciadorDeProdutos.service;

import com.join.GerenciadorDeProdutos.model.entity.Category;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface CategoryServiceInterface {
  Page<Category> findAllCategories(int pageNumber, int pageSize, String query);
  Category findCategoryById(UUID categoryId);
  Category createCategory(Category category);
  Category updateCategoryById(UUID categoryId, Category category);
  void deleteCategoryById(UUID categoryId);
}
