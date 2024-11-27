import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CategoryService } from '../../services/category.service';
import { Router } from '@angular/router';
import { Category } from '../../models/category.model';

@Component({
  selector: 'app-form-category',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './form-category.component.html',
  styleUrl: './form-category.component.css'
})
export class FormCategoryComponent implements OnInit {
  @Input()
  action!: 'create' | 'edit';

  @Input()
  categoryId!: string | null;

  categoryForm!: FormGroup;

  category!: Category;

  constructor(
    private fb: FormBuilder,
    private categoryService: CategoryService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.categoryForm = this.fb.group({
      name: ['', Validators.required],
      products: [[], Validators.required]
    });

    if (this.action === 'edit' && this.categoryId) {
      this.categoryService.getCategoryById(this.categoryId).subscribe(data => {
        this.category = data;
        this.categoryForm.patchValue(this.category);
      });
    }
  }

  onSubmit(): void {
    const { name } = this.categoryForm.value
    const categoryFormOutput = {
      name
    }
    if (this.action === 'edit' && this.categoryId) {
      this.categoryService.updateCategory(this.categoryId, categoryFormOutput).subscribe(() => {
        this.router.navigate(['/categories']);
      });
    } else {
      this.categoryService.createCategory(categoryFormOutput).subscribe(() => {
        this.router.navigate(['/categories']);
      });
    }
  }

  onCancel(): void {
    this.router.navigate(['/categories']);
  }
}
