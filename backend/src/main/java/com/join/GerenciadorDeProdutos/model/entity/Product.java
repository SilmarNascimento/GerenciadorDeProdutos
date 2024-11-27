package com.join.GerenciadorDeProdutos.model.entity;

import com.join.GerenciadorDeProdutos.controller.dto.product.ProductInputDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Product")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Product {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "price", nullable = false)
  private Double price;

  @ManyToMany
  @JoinTable(
      name = "product_categories",
      joinColumns = @JoinColumn(name = "categories_id"),
      inverseJoinColumns = @JoinColumn(name = "product_id")
  )
  @Column(name = "categories")
  private List<Category> categories;

  public static Product parseProduct(ProductInputDto inputDto) {
    return Product.builder()
        .name(inputDto.name())
        .description(inputDto.description())
        .price(inputDto.price())
        .build();
  }
}
