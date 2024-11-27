import { Component } from '@angular/core';
import { TableFooterComponent } from '../../../components/table-footer/table-footer.component';
import { NavbarComponent } from "../../../components/navbar/navbar.component";

@Component({
  selector: 'app-categories-list',
  imports: [TableFooterComponent, NavbarComponent],
  templateUrl: './categories-list.component.html',
  styleUrl: './categories-list.component.css'
})
export class CategoriesListComponent {

}
