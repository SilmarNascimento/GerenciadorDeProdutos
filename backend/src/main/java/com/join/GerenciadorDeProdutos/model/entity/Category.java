package com.join.GerenciadorDeProdutos.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.join.GerenciadorDeProdutos.controller.dto.category.CategoryInputDto;
import com.join.GerenciadorDeProdutos.exception.InvalidArgumentException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Category")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Category {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  private String name;

  @ManyToMany(mappedBy = "categories")
  @JsonIgnore
  private List<Product> products;

  @Override
  public String toString() {
    return "{" +
        "id: " + this.id +
        "name: " + this.name +
        '}';
  }

  public static Category parseCategory(CategoryInputDto inputDto) {
    return Category.builder()
        .name(inputDto.name())
        .build();
  }

  public void validate() {
    if (this.name == null || this.name.isBlank()) {
      throw new InvalidArgumentException("Nome da categoria não pode ser nulo ou vazio!");
    }
  }
}
