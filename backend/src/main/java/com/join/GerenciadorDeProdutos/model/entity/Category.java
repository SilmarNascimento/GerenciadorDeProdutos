package com.join.GerenciadorDeProdutos.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Product")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Category {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  private String nome;

  @ManyToMany(mappedBy = "categories")
  @JsonIgnore
  private List<Product> products;
}
