import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductsListComponent } from './pages/products/products-list/products-list.component';
import { ProductsCreateEditComponent } from './pages/products/products-create-edit/products-create-edit.component';
import { CategoriesListComponent } from './pages/categories/categories-list/categories-list.component';
import { CategoriesCreateComponent } from './pages/categories/categories-create/categories-create.component';
import { CategoriesEditComponent } from './pages/categories/categories-edit/categories-edit.component';

export const routes: Routes = [
  { path: '', redirectTo: '/products', pathMatch: 'full' },
  { path: 'products', component: ProductsListComponent },
  { path: 'products/create', component: ProductsCreateEditComponent },
  { path: 'products/edit/:id', component: ProductsCreateEditComponent },
  { path: 'categories', component: CategoriesListComponent },
  { path: 'categories/create', component: CategoriesCreateComponent },
  { path: 'categories/edit/:id', component: CategoriesEditComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
