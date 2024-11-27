import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Product } from '../../models/product.model';
import { CategoryService } from '../../services/category.service';
import { ProductService } from '../../services/product.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Category } from '../../models/category.model';

@Component({
  selector: 'app-form-product',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './form-product.component.html',
  styleUrl: './form-product.component.css'
})
export class FormProductComponent implements OnInit {
  @Input()
  action!: 'create' | 'edit';

  @Input()
  productId!: string | null;

  productForm!: FormGroup;

  categories: Category[] = [];

  product!: Product;

  constructor(
    private fb: FormBuilder,
    private categoryService: CategoryService,
    private productService: ProductService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.productForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      price: ['', Validators.required],
      categories: [[], Validators.required]
    });

    this.categoryService.getCategories(1, 100).subscribe(data => {
      this.categories = data.data;
    });

    if (this.action === 'edit' && this.productId) {
      this.productService.getProductById(this.productId).subscribe(data => {
        this.product = data;
        this.productForm.patchValue(this.product);
      });
    }
  }

  onSubmit(): void {
    if (this.action === 'edit' && this.productId) {
      this.productService.updateProduct(this.productId, this.productForm.value).subscribe(() => {
        this.router.navigate(['/products']);
      });
    } else {
      this.productService.createProduct(this.productForm.value).subscribe(() => {
        this.router.navigate(['/products']);
      });
    }
  }

  onCancel(): void {
    this.router.navigate(['/products']);
  }
}
