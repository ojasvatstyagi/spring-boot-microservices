import {Component, inject, OnInit} from '@angular/core';
import {OidcSecurityService} from "angular-auth-oidc-client";
import {Product} from "../../model/product";
import {ProductService} from "../../services/product/product.service";
import {CommonModule, AsyncPipe, JsonPipe, NgIf} from "@angular/common";
import {Router} from "@angular/router";
import {Order} from "../../model/order";
import {FormsModule} from "@angular/forms";
import {OrderService} from "../../services/order/order.service";

@Component({
  selector: 'app-homepage',
  templateUrl: './home-page.component.html',
  standalone: true,
  imports: [
    AsyncPipe,
    JsonPipe,
    FormsModule,
    NgIf,
    CommonModule
  ],
  styleUrl: './home-page.component.css'
})
export class HomePageComponent implements OnInit {
  public readonly oidcSecurityService = inject(OidcSecurityService);
  private readonly productService = inject(ProductService);
  private readonly orderService = inject(OrderService);
  private readonly router = inject(Router);

  isAuthenticated = false;
  products: Array<Product> = [];
  quantityIsNull = false;
  orderSuccess = false;
  orderFailed = false;

  ngOnInit(): void {
    this.oidcSecurityService.isAuthenticated$.subscribe(
      ({isAuthenticated}) => {
        this.isAuthenticated = isAuthenticated;
        // Only fetch products if the user is authenticated
        if (isAuthenticated) {
          this.loadProducts(); // Call a new method to load products
        } else {
          this.products = [];
          this.resetOrderMessages();
        }
      }
    );
  }

  private loadProducts(): void {
    this.productService.getProducts()
      .subscribe(product => {
        this.products = product;
        this.resetOrderMessages();
      }, error => {
        console.error('Error fetching products:', error);
        this.products = [];
      });
  }

  goToCreateProductPage(): void {
    this.router.navigateByUrl('/add-product'); // Changed to /add-product as per routes
  }

  // New method to navigate to the edit product page
  editProduct(productId: string | undefined): void {
    if (productId) {
      this.router.navigateByUrl(`/edit-product/${productId}`);
    } else {
      console.warn('Cannot edit product: Product ID is missing.');
    }
  }

  // New method to delete a product
  deleteProduct(productId: string | undefined): void {
    if (productId && confirm('Are you sure you want to delete this product?')) {
      this.productService.deleteProduct(productId).subscribe({
        next: () => {
          console.log('Product deleted successfully!');
          this.loadProducts(); // Refresh the product list after deletion
        },
        error: (err) => {
          console.error('Error deleting product:', err);
          alert('Failed to delete product. Please try again.');
        }
      });
    }
  }


  orderProduct(product: Product, quantity: string): void {
    this.resetOrderMessages();

    if (!quantity || Number(quantity) <= 0) {
      this.orderFailed = true;
      this.quantityIsNull = true;
      return;
    }

    this.oidcSecurityService.userData$.subscribe(result => {
      if (result.userData) {
        const userDetails = {
          email: result.userData.email,
          firstName: result.userData.firstName,
          lastName: result.userData.lastName
        };

        const order: Order = {
          skuCode: product.skuCode,
          price: product.price,
          quantity: Number(quantity),
          userDetails: userDetails
        };

        this.orderService.orderProduct(order).subscribe(() => {
          this.orderSuccess = true;
          this.orderFailed = false;
        }, error => {
          console.error('Order failed:', error);
          this.orderFailed = true;
          this.orderSuccess = false;
        });
      } else {
        console.warn('User data not available for ordering.');
        this.orderFailed = true;
      }
    });
  }

  private resetOrderMessages(): void {
    this.orderSuccess = false;
    this.orderFailed = false;
    this.quantityIsNull = false;
  }
}
