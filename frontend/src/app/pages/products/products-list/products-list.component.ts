import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { TableProductComponent } from "../../../components/table-product/table-product.component";
import { NavbarComponent } from '../../../components/navbar/navbar.component';
import { TableFooterComponent } from '../../../components/table-footer/table-footer.component';
import { Product } from '../../../models/product.model';
import { ProductService } from '../../../services/product.service';
import { PaginatedInputDto } from '../../../models/paginated-input.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-products-list',
  imports: [TableProductComponent, NavbarComponent, TableFooterComponent],
  templateUrl: './products-list.component.html',
  styleUrl: './products-list.component.css'
})
export class ProductsListComponent implements OnInit {
  products: Product[] = [];
  totalItems: number = 0;
  currentPage: number = 1;
  pageSize: number = 5;

  constructor(
    private router: Router,
    private productService: ProductService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadProducts(this.currentPage, this.pageSize);
  }

  loadProducts(page: number, size:number): void {
    this.productService.getProducts(page, size).subscribe({
      next: (response: PaginatedInputDto<Product>) => {
        console.log("resposta da API: ", response);

        this.products = response.data;
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
    this.loadProducts(newPage, this.pageSize);
  }

  onItemsPerPageChange(newPageSize: number): void {
    this.pageSize = newPageSize;
    this.currentPage = 1;
    this.loadProducts(this.currentPage, this.pageSize);
  }

  createProduct(): void {
    this.router.navigate(['/products/create']);
  }
}
