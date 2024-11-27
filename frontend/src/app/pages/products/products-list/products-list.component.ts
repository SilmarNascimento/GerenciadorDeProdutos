import { Component } from '@angular/core';
import { TableProductComponent } from "../../../components/table-product/table-product.component";
import { NavbarComponent } from '../../../components/navbar/navbar.component';
import { TableFooterComponent } from '../../../components/table-footer/table-footer.component';

@Component({
  selector: 'app-products-list',
  imports: [TableProductComponent, NavbarComponent, TableFooterComponent],
  templateUrl: './products-list.component.html',
  styleUrl: './products-list.component.css'
})
export class ProductsListComponent {

}
