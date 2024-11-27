import { Component, OnInit } from '@angular/core';
import { FormProductComponent } from '../../../components/form-product/form-product.component';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-products-create-edit',
  imports: [FormProductComponent],
  templateUrl: './products-create-edit.component.html',
  styleUrl: './products-create-edit.component.css'
})
export class ProductsCreateEditComponent implements OnInit {
  action!: 'create' | 'edit';
  productId: string | null = null;

  constructor(
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.url.subscribe(segments => {
      if (segments.some(segment => segment.path === 'create')) {
        this.action = 'create';
      } else if (segments.some(segment => segment.path === 'edit')) {
        this.action = 'edit';
        this.productId = this.route.snapshot.paramMap.get('id');
      }
    });
  }
}
