import { ChangeDetectorRef, Component } from '@angular/core';
import { TableFooterComponent } from '../../../components/table-footer/table-footer.component';
import { NavbarComponent } from "../../../components/navbar/navbar.component";
import { Category } from '../../../models/category.model';
import { Router } from '@angular/router';
import { CategoryService } from '../../../services/category.service';
import { PaginatedInputDto } from '../../../models/paginated-input.model';
import { TableCategoryComponent } from "../../../components/table-category/table-category.component";

@Component({
  selector: 'app-categories-list',
  imports: [TableFooterComponent, NavbarComponent, TableCategoryComponent],
  templateUrl: './categories-list.component.html',
  styleUrl: './categories-list.component.css'
})
export class CategoriesListComponent {
  categories: Category[] = [];
  totalItems: number = 0;
  currentPage: number = 1;
  pageSize: number = 5;

  constructor(
    private router: Router,
    private categoryService: CategoryService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadCategories(this.currentPage, this.pageSize);
  }

  loadCategories(page: number, size:number): void {
    this.categoryService.getCategories(page, size).subscribe({
      next: (response: PaginatedInputDto<Category>) => {
        console.log("resposta da API: ", response);

        this.categories = response.data;
        this.totalItems = response.totalItems;
        this.currentPage = response.currentPage + 1;

        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Erro ao carregar produtos:', err);
      },
    });
  }

  onPageChange(newPage: number): void {
    this.currentPage = newPage;
    this.loadCategories(newPage, this.pageSize);
  }

  onItemsPerPageChange(newPageSize: number): void {
    this.pageSize = newPageSize;
    this.currentPage = 1;
    this.loadCategories(this.currentPage, this.pageSize);
  }

  createCategory(): void {
    this.router.navigate(['/categories/create']);
  }
}
