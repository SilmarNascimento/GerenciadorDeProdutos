export interface PaginatedInputDto<T> {
  pageItems: number;
  totalItems: number;
  currentPage: number;
  pages: number;
  data: T[];
}
