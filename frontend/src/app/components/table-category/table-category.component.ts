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
  productDeleted = new EventEmitter<void>();

  constructor(private router: Router, private categoryService: CategoryService) { }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['products']) {
      console.log('Produtos recebidos no componente filho:', this.categories);
    }
  }

  editCategory(productId: string | undefined): void {
    if (productId) {
      this.router.navigate(['/products/edit', productId]);
    }
  }

  deleteCategory(productId: string | undefined): void {
    if (productId) {
      this.categoryService.deleteCategory(productId).subscribe(() => {
        this.productDeleted.emit();
      });
    }
  }
}
