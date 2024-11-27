import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormCategoryComponent } from '../../../components/form-category/form-category.component';

@Component({
  selector: 'app-categories-create-edit',
  imports: [FormCategoryComponent],
  templateUrl: './categories-create-edit.component.html',
  styleUrl: './categories-create-edit.component.css'
})
export class CategoriesCreateEditComponent implements OnInit {
  action!: 'create' | 'edit';
  categoryId: string | null = null;

  constructor(
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.url.subscribe(segments => {
      if (segments.some(segment => segment.path === 'create')) {
        this.action = 'create';
      } else if (segments.some(segment => segment.path === 'edit')) {
        this.action = 'edit';
        this.categoryId = this.route.snapshot.paramMap.get('id');
      }
    });
  }
}
