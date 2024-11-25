package com.join.GerenciadorDeProdutos.model.repository;

import com.join.GerenciadorDeProdutos.model.entity.Product;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, UUID> {

}
