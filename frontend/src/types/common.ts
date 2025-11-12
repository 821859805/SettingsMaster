export interface Result<T> {
  code: number;
  message: string;
  data: T | null;
}

export interface PagedResult<T> {
  items: T[];
  total: number;
  page: number;
  pageSize: number;
}
