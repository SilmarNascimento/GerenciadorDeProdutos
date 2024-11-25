package com.join.GerenciadorDeProdutos.controller;

import com.join.GerenciadorDeProdutos.service.CategoryServiceInterface;
import com.join.GerenciadorDeProdutos.service.ProductServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
  private final ProductServiceInterface productService;
  private  final CategoryServiceInterface categoryService;

}
