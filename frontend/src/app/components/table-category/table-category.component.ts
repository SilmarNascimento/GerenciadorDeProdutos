import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { Category } from '../../models/category.model';
import { Router } from '@angular/router';
import { CategoryService } from '../../services/category.service';

@Component({
  selector: 'app-table-category',
  imports: [],
  templateUrl: './table-category.component.html',
  styleUrl: './table-category.component.css'
})
export class TableCategoryComponent implements OnChanges{
  @Input()
  categories: Category[] = [];

  @Output()
  categoryDeleted = new EventEmitter<void>();

  constructor(private router: Router, private categoryService: CategoryService) { }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['categories']) {
      console.log('Categorias recebidos no componente filho:', this.categories);
    }
  }

  editCategory(categoryId: string | undefined): void {
    if (categoryId) {
      this.router.navigate(['/categories/edit', categoryId]);
    }
  }

  deleteCategory(categoryId: string | undefined): void {
    if (categoryId) {
      this.categoryService.deleteCategory(categoryId).subscribe(() => {
        this.categoryDeleted.emit();
      });
    }
  }
}
