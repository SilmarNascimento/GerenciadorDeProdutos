import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductsListComponent } from './pages/products/products-list/products-list.component';
import { ProductsCreateComponent } from './pages/products/products-create/products-create.component';
import { ProductsEditComponent } from './pages/products/products-edit/products-edit.component';
import { CategoriesListComponent } from './pages/categories/categories-list/categories-list.component';
import { CategoriesCreateComponent } from './pages/categories/categories-create/categories-create.component';
import { CategoriesEditComponent } from './pages/categories/categories-edit/categories-edit.component';

const routes: Routes = [
  { path: '', redirectTo: '/products', pathMatch: 'full' },
  { path: 'products', component: ProductsListComponent },
  { path: 'products/create', component: ProductsCreateComponent },
  { path: 'products/edit/:id', component: ProductsEditComponent },
  { path: 'categories', component: CategoriesListComponent },
  { path: 'categories/create', component: CategoriesCreateComponent },
  { path: 'categories/edit/:id', component: CategoriesEditComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
