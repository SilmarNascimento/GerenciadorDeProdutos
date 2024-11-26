package com.join.GerenciadorDeProdutos.model.repository;

import com.join.GerenciadorDeProdutos.model.entity.Product;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, UUID> {
  @Query("SELECT product FROM Product product WHERE " +
      "(:query IS NULL OR product.name LIKE %:query%) " +
      "ORDER BY product.name ASC")
  Page<Product> findAllOrderByName(@NonNull Pageable pageable, String query);

  Optional<Product> findByName(String name);
}