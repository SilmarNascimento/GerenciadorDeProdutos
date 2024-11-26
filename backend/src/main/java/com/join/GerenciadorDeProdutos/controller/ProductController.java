package com.join.GerenciadorDeProdutos.controller;

import com.join.GerenciadorDeProdutos.controller.dto.PageOutputDto;
import com.join.GerenciadorDeProdutos.controller.dto.category.CategoryOutputDto;
import com.join.GerenciadorDeProdutos.controller.dto.product.ProductInputDto;
import com.join.GerenciadorDeProdutos.controller.dto.product.ProductOutputDto;
import com.join.GerenciadorDeProdutos.model.entity.Product;
import com.join.GerenciadorDeProdutos.service.CategoryServiceInterface;
import com.join.GerenciadorDeProdutos.service.ProductServiceInterface;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
  private final ProductServiceInterface productService;
  private  final CategoryServiceInterface categoryService;

  @GetMapping
  public ResponseEntity<PageOutputDto<ProductOutputDto>> findAllProducts(
      @RequestParam(required = false, defaultValue = "0") int pageNumber,
      @RequestParam(required = false, defaultValue = "20") int pageSize,
      @RequestParam(required = false) String query
  ) {
    Page<Product> productsPage = productService.findAllProducts(pageNumber, pageSize, query);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(PageOutputDto.parseDto(
            productsPage,
            ProductOutputDto::parseDto
        ));
  }

  @GetMapping("/{productId}")
  public ResponseEntity<ProductOutputDto> findProductById(@PathVariable UUID productId) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(ProductOutputDto.parseDto(productService.findProductById(productId)));
  }

  @PostMapping
  public ResponseEntity<ProductOutputDto> createProduct(@RequestBody ProductInputDto productInputDto) {
    Product productCreated = productService.createProduct(
        Product.parseProduct(productInputDto),
        productInputDto.categoryIdList()
    );
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(ProductOutputDto.parseDto(productCreated));
  }

  @PutMapping("/{productId}")
  public ResponseEntity<ProductOutputDto> updateProductById(
      @PathVariable UUID productId,
      @RequestBody ProductInputDto productInputDto
  ) {
    Product updatedProduct = productService.updateProductById(
        productId,
        Product.parseProduct(productInputDto),
        productInputDto.categoryIdList()
    );

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(ProductOutputDto.parseDto(updatedProduct));
  }

  @DeleteMapping("/{productId}")
  public ResponseEntity<Void> deleteProductById(@PathVariable UUID productId) {
    productService.deleteProductById(productId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }
}
