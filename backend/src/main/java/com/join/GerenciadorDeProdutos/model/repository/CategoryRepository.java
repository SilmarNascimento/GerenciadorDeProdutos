package com.join.GerenciadorDeProdutos.model.repository;

import com.join.GerenciadorDeProdutos.model.entity.Category;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

}
