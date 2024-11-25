package com.join.GerenciadorDeProdutos.model.repository;

import com.join.GerenciadorDeProdutos.model.entity.Category;
import com.join.GerenciadorDeProdutos.model.entity.Product;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
  @Query("SELECT subject FROM Subject subject WHERE (:query IS NULL OR subject.name LIKE %:query%) ORDER BY subject.name ASC")
  Page<Category> findAllOrderByName(@NonNull Pageable pageable, String query);

  Optional<Category> findByName(String name);
}
