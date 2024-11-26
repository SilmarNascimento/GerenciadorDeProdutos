package com.join.GerenciadorDeProdutos.service.implementation;

import com.join.GerenciadorDeProdutos.exception.AlreadyExistsException;
import com.join.GerenciadorDeProdutos.exception.NotFoundException;
import com.join.GerenciadorDeProdutos.model.entity.Category;
import com.join.GerenciadorDeProdutos.model.repository.CategoryRepository;
import com.join.GerenciadorDeProdutos.service.CategoryServiceInterface;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryServiceInterface {
  private final CategoryRepository categoryRepository;

  @Override
  public Page<Category> findAllCategories(int pageNumber, int pageSize, String query) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    if (query == null || query.isBlank()) {
      return categoryRepository.findAllOrderByName(pageable, null);
    }
    return categoryRepository.findAllOrderByName(pageable, query.toLowerCase());
  }

  @Override
  public Category findCategoryById(UUID categoryId) {
    return categoryRepository.findById(categoryId)
        .orElseThrow(() -> new NotFoundException("Categoria não encontrada!"));
  }

  @Override
  public Category createCategory(Category category) {
    categoryRepository.findByName(category.getName().toLowerCase())
        .ifPresent(categoryFound -> {
          throw new AlreadyExistsException("Categoria já cadastrada!");
        });

    category.validate();

    category.setName(category.getName().toLowerCase());

    return categoryRepository.save(category);
  }

  @Override
  public Category updateCategoryById(UUID categoryId, Category category) {
    Category categoryFound = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new NotFoundException("Categoria não encontrada!"));

    category.validate();

    categoryFound.setName(category.getName().toLowerCase());

    return categoryRepository.save(categoryFound);
  }

  @Override
  public void deleteCategoryById(UUID categoryId) {
    categoryRepository.findById(categoryId)
        .orElseThrow(() -> new NotFoundException("Categoria não encontrada!"));

    categoryRepository.deleteById(categoryId);
  }
}
