package com.join.GerenciadorDeProdutos.service.implementation;

import com.join.GerenciadorDeProdutos.exception.AlreadyExistsException;
import com.join.GerenciadorDeProdutos.exception.InvalidArgumentException;
import com.join.GerenciadorDeProdutos.exception.NotFoundException;
import com.join.GerenciadorDeProdutos.model.entity.Category;
import com.join.GerenciadorDeProdutos.model.entity.Product;
import com.join.GerenciadorDeProdutos.model.repository.CategoryRepository;
import com.join.GerenciadorDeProdutos.model.repository.ProductRepository;
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
  private final ProductRepository productRepository;

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

    validateInput(category);

    category.setName(category.getName().toLowerCase());

    return categoryRepository.save(category);
  }

  @Override
  public Category updateCategoryById(UUID categoryId, Category category) {
    Category categoryFound = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new NotFoundException("Categoria não encontrada!"));

    validateInput(category);

    categoryFound.setName(category.getName().toLowerCase());

    return categoryRepository.save(categoryFound);
  }

  @Override
  public void deleteCategoryById(UUID categoryId) {
    Category categoryFound = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new NotFoundException("Categoria não encontrada!"));

    if (!categoryFound.getProducts().isEmpty()) {
      categoryFound.removeCategoryFromProducts();

      for (Product product : categoryFound.getProducts()) {
        productRepository.save(product);
      }
    }

    categoryRepository.deleteById(categoryId);
  }

  private static void validateInput(Category category) {
    if (category.getName() == null || category.getName().isBlank()) {
      throw new InvalidArgumentException("Nome da categoria não pode ser nulo ou vazio!");
    }
  }
}
