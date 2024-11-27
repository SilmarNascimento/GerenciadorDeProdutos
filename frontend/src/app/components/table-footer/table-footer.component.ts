import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-table-footer',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './table-footer.component.html',
  styleUrl: './table-footer.component.css'
})
export class TableFooterComponent {
  @Input()
  totalItems = 100;

  @Input()
  itemsPerPage = 10;

  @Input()
  currentPage = 1;

  @Input()
  itemsPerPageOptions = [5, 10, 20];

  @Output()
  pageChanged = new EventEmitter<number>();

  @Output()
  itemsPerPageChanged = new EventEmitter<number>();

  get totalPages(): number {
    return Math.ceil(this.totalItems / this.itemsPerPage);
  }

  goToFirstPage() {
    this.currentPage = 1;
    this.pageChanged.emit(this.currentPage);
  }

  goToPrevPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.pageChanged.emit(this.currentPage);
    }
  }

  goToNextPage() {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.pageChanged.emit(this.currentPage);
    }
  }

  goToLastPage() {
    this.currentPage = this.totalPages;
    this.pageChanged.emit(this.currentPage);
  }

  onItemsPerPageChange(event: Event) {
    const selectElement = event.target as HTMLSelectElement;
    this.itemsPerPage = parseInt(selectElement.value, 10);
    this.currentPage = 1;
    this.itemsPerPageChanged.emit(this.itemsPerPage);
    this.pageChanged.emit(this.currentPage);
  }
}
