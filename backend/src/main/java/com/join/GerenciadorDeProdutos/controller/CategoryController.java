package com.join.GerenciadorDeProdutos.controller;

import com.join.GerenciadorDeProdutos.controller.dto.PageOutputDto;
import com.join.GerenciadorDeProdutos.controller.dto.category.CategoryInputDto;
import com.join.GerenciadorDeProdutos.controller.dto.category.CategoryOutputDto;
import com.join.GerenciadorDeProdutos.model.entity.Category;
import com.join.GerenciadorDeProdutos.service.CategoryServiceInterface;
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
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
  private final CategoryServiceInterface categoryService;

  @GetMapping
  public ResponseEntity<PageOutputDto<CategoryOutputDto>> findAllCategories(
      @RequestParam(required = false, defaultValue = "0") int pageNumber,
      @RequestParam(required = false, defaultValue = "20") int pageSize,
      @RequestParam(required = false) String query
  ) {
    Page<Category> categoriesPage = categoryService.findAllCategories(pageNumber, pageSize, query);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(PageOutputDto.parseDto(
            categoriesPage,
            CategoryOutputDto::parseDto
        ));
  }

  @GetMapping("/{categoryId}")
  public ResponseEntity<CategoryOutputDto> findCategoryById(@PathVariable UUID categoryId) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(CategoryOutputDto.parseDto(categoryService.findCategoryById(categoryId)));
  }

  @PostMapping
  public ResponseEntity<CategoryOutputDto> createCategory(@RequestBody CategoryInputDto categoryInputDto) {
    Category categoryCreated = categoryService.createCategory(Category.parseCategory(categoryInputDto));
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(CategoryOutputDto.parseDto(categoryCreated));
  }

  @PutMapping("/{categoryId}")
  public ResponseEntity<CategoryOutputDto> updateCategoryById(
      @PathVariable UUID categoryId,
      @RequestBody CategoryInputDto categoryInputDto
  ) {
    Category updatedCategory = categoryService.updateCategoryById(
        categoryId,
        Category.parseCategory(categoryInputDto)
    );

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(CategoryOutputDto.parseDto(updatedCategory));
  }

  @DeleteMapping("/{categoryId}")
  public ResponseEntity<Void> deleteCategoryById(@PathVariable UUID categoryId) {
    categoryService.deleteCategoryById(categoryId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }
}
