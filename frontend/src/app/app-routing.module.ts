import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductsListComponent } from './pages/products/products-list/products-list.component';
import { ProductsCreateEditComponent } from './pages/products/products-create-edit/products-create-edit.component';
import { CategoriesListComponent } from './pages/categories/categories-list/categories-list.component';
import { CategoriesCreateEditComponent } from './pages/categories/categories-create-edit/categories-create-edit.component';

export const routes: Routes = [
  { path: '', redirectTo: '/products', pathMatch: 'full' },
  { path: 'products', component: ProductsListComponent },
  { path: 'products/create', component: ProductsCreateEditComponent },
  { path: 'products/edit/:id', component: ProductsCreateEditComponent },
  { path: 'categories', component: CategoriesListComponent },
  { path: 'categories/create', component: CategoriesCreateEditComponent },
  { path: 'categories/edit/:id', component: CategoriesCreateEditComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
