import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Product } from '../../models/product.model';
import { Router } from '@angular/router';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-table-product',
  standalone: true,
  templateUrl: './table-product.component.html',
  styleUrl: './table-product.component.css'
})
export class TableProductComponent {
  @Input()
  products: Product[] = [];

  @Output()
  productDeleted = new EventEmitter<void>();

  constructor(private router: Router, private productService: ProductService) {}

  editProduct(productId: string | undefined): void {
    if (productId) {
      this.router.navigate(['/products/edit', productId]);
    }
  }

  deleteProduct(productId: string | undefined): void {
    if (productId) {
      this.productService.deleteProduct(productId).subscribe(() => {
        this.productDeleted.emit();
      });
    }
  }
}
