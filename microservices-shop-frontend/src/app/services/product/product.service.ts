import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Product} from "../../model/product";

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private apiUrl = 'http://localhost:9000/api/product'; // Define base API URL

  constructor(private httpClient: HttpClient) {
  }

  getProducts(): Observable<Array<Product>> {
    return this.httpClient.get<Array<Product>>(this.apiUrl);
  }

  // New: Get a single product by ID
  getProductById(id: string): Observable<Product> {
    return this.httpClient.get<Product>(`${this.apiUrl}/${id}`);
  }

  createProduct(product: Product): Observable<Product> {
    return this.httpClient.post<Product>(this.apiUrl, product);
  }

  // New: Update an existing product
  updateProduct(id: string, product: Product): Observable<Product> {
    return this.httpClient.put<Product>(`${this.apiUrl}/${id}`, product);
  }

  // New: Delete a product
  deleteProduct(id: string): Observable<void> { // Using Observable<void> as the backend returns no content
    return this.httpClient.delete<void>(`${this.apiUrl}/${id}`);
  }
}
