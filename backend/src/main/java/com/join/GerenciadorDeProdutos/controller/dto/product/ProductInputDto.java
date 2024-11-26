package com.join.GerenciadorDeProdutos.controller.dto.product;

import com.join.GerenciadorDeProdutos.model.entity.Category;
import java.util.List;
import java.util.UUID;

public record ProductInputDto(
    String name,
    String description,
    Double price,
    List<UUID> categoryIdList
) {

}
