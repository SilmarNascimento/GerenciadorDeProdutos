package com.join.GerenciadorDeProdutos.model.entity;

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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Product")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  private String name;

  private String description;

  private Double price;

  @ManyToMany
  @JoinTable(
      name = "product_categories",
      joinColumns = @JoinColumn(name = "categories_id"),
      inverseJoinColumns = @JoinColumn(name = "product_id")
  )
  private List<Category> categories;
}
