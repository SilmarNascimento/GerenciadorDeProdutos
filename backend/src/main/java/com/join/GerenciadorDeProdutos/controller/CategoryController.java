package com.join.GerenciadorDeProdutos.controller;

import com.join.GerenciadorDeProdutos.service.CategoryServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
  private final CategoryServiceInterface categoryService;


}
