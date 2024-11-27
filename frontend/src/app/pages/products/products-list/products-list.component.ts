import { Component, OnInit } from '@angular/core';
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
  pageSize: number = 10;

  constructor(private router: Router, private productService: ProductService) { }

  ngOnInit(): void {
    this.loadProducts(this.currentPage, this.pageSize);
  }

  loadProducts(page: number, size:number): void {
    this.productService.getProducts(page, size).subscribe((response: PaginatedInputDto<Product>) => {
      this.products = response.data;
      this.totalItems = response.totalItems;
      this.currentPage = response.currentPage;
    });
  }

  createProduct(): void {
    this.router.navigate(['/products/create']);
  }

  changePage(page: number): void {
    this.loadProducts(page, this.pageSize);
  }
}
