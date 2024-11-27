import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PaginatedInputDto } from '../models/paginated-input.model';
import { Category } from '../models/category.model';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private apiUrl = 'http://localhost:8080/api/v1/categories';

  constructor(private http: HttpClient) { }

  getProducts(page: number, size: number): Observable<PaginatedInputDto<Category>> {
    const params = { pageNumber: (page - 1).toString(), pageSize: size.toString() };
    return this.http.get<PaginatedInputDto<Category>>(this.apiUrl, { params });
  }

  getProductById(id: string): Observable<Category> {
    return this.http.get<Category>(`${this.apiUrl}/${id}`);
  }

  createProduct(category: Category): Observable<Category> {
    return this.http.post<Category>(this.apiUrl, category);
  }

  updateProduct(id: string, category: Category): Observable<Category> {
    return this.http.put<Category>(`${this.apiUrl}/${id}`, category);
  }

  deleteProduct(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
