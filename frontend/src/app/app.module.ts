import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { TableFooterComponent } from './components/table-footer/table-footer.component';
import { ProductsListComponent } from './pages/products/products-list/products-list.component';
import { ProductsCreateComponent } from './pages/products/products-create/products-create.component';
import { ProductsEditComponent } from './pages/products/products-edit/products-edit.component';
import { CategoriesListComponent } from './pages/categories/categories-list/categories-list.component';
import { CategoriesCreateComponent } from './pages/categories/categories-create/categories-create.component';
import { CategoriesEditComponent } from './pages/categories/categories-edit/categories-edit.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    TableFooterComponent,
    ProductsListComponent,
    ProductsCreateComponent,
    ProductsEditComponent,
    CategoriesListComponent,
    CategoriesCreateComponent,
    CategoriesEditComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
