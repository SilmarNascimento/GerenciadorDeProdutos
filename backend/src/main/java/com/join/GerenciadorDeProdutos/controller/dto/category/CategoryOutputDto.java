package com.join.GerenciadorDeProdutos.controller.dto.category;

import com.join.GerenciadorDeProdutos.model.entity.Category;
import java.util.List;
import java.util.UUID;

public record CategoryOutputDto(
    UUID id,
    String name
) {
  public static CategoryOutputDto parseDto(Category category) {
    return new CategoryOutputDto(
        category.getId(),
        category.getName()
    );
  }

  public static List<CategoryOutputDto>  parseDto(List<Category> categories) {
    return categories.stream()
        .map((Category category) -> new CategoryOutputDto(category.getId(), category.getName()))
        .toList();
  }
}
