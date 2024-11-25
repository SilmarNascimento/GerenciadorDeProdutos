package com.join.GerenciadorDeProdutos.service.implementation;

import com.join.GerenciadorDeProdutos.model.entity.Category;
import com.join.GerenciadorDeProdutos.service.CategoryServiceInterface;
import java.util.UUID;
import org.springframework.data.domain.Page;

public class CategoryService implements CategoryServiceInterface {

  @Override
  public Page<Category> findAllCategories(int pageNumber, int pageSize, String query) {
    return null;
  }

  @Override
  public Category findCategoryById(UUID productId) {
    return null;
  }

  @Override
  public Category createCategory(Category product) {
    return null;
  }

  @Override
  public Category updateCategoryById(UUID productId, Category product) {
    return null;
  }

  @Override
  public void deleteCategory(UUID productId) {

  }
}
