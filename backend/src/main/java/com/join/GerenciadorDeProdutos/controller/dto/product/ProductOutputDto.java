package com.join.GerenciadorDeProdutos.controller.dto.product;

import com.join.GerenciadorDeProdutos.controller.dto.category.CategoryOutputDto;
import com.join.GerenciadorDeProdutos.model.entity.Category;
import com.join.GerenciadorDeProdutos.model.entity.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record ProductOutputDto(
    UUID id,
    String name,
    String description,
    Double price,
    List<Category> categories
) {
  public static ProductOutputDto parseDto(Product product) {
    if (product.getCategories().isEmpty()) {
      return new ProductOutputDto(
          product.getId(),
          product.getName(),
          product.getDescription(),
          product.getPrice(),
          new ArrayList<>()
      );
    }

    return new ProductOutputDto(
        product.getId(),
        product.getName(),
        product.getDescription(),
        product.getPrice(),
        product.getCategories()
    );
  }

  public static List<CategoryOutputDto>  parseDto(List<Category> categories) {
    return categories.stream()
        .map((Category category) -> new CategoryOutputDto(category.getId(), category.getName()))
        .toList();
  }
}
