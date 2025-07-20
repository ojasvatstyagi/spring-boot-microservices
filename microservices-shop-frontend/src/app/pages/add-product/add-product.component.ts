import {Component, inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Product} from "../../model/product";
import {ProductService} from "../../services/product/product.service";
import {NgIf, NgClass} from "@angular/common";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-add-product',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf, NgClass],
  templateUrl: './add-product.component.html',
  styleUrl: './add-product.component.css'
})
export class AddProductComponent implements OnInit {
  addProductForm: FormGroup;
  private readonly productService = inject(ProductService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  operationSuccess = false;
  isEditMode = false;
  productId: string | null = null;

  constructor(private fb: FormBuilder) {
    this.addProductForm = this.fb.group({
      skuCode: ['', [Validators.required, Validators.minLength(3)]],
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required, Validators.minLength(10)]],
      price: [0, [Validators.required, Validators.min(0.01)]],
      quantity: [0, [Validators.required, Validators.min(0)]] // <--- NEW: Add quantity with validator
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.productId = params.get('id');
      if (this.productId) {
        this.isEditMode = true;
        this.productService.getProductById(this.productId).subscribe({
          next: (product) => {
            // Populate the form with existing product data, including quantity
            this.addProductForm.patchValue(product);
          },
          error: (err) => {
            console.error('Failed to load product for editing:', err);
            this.router.navigateByUrl('/'); // Redirect to home if product not found
          }
        });
      }
    });
  }

  onSubmit(): void {
    this.operationSuccess = false;

    if (this.addProductForm.valid) {
      const product: Product = this.addProductForm.value;

      if (this.isEditMode && this.productId) {
        this.productService.updateProduct(this.productId, product).subscribe({
          next: () => {
            this.operationSuccess = true;
            console.log('Product updated successfully!');
            setTimeout(() => this.router.navigateByUrl('/'), 2000);
          },
          error: (err) => {
            console.error('Error updating product:', err);
            // Handle error
          }
        });
      } else {
        this.productService.createProduct(product).subscribe({
          next: () => {
            this.operationSuccess = true;
            this.addProductForm.reset();
            console.log('Product created successfully!');
            setTimeout(() => this.router.navigateByUrl('/'), 2000);
          },
          error: (err) => {
            console.error('Error creating product:', err);
            // Handle error
          }
        });
      }
    } else {
      console.log('Form is not valid. Please check all fields.');
      this.addProductForm.markAllAsTouched();
    }
  }

  // New getter for quantity
  get quantity() {
    return this.addProductForm.get('quantity');
  }

  // Existing getters
  get skuCode() {
    return this.addProductForm.get('skuCode');
  }

  get name() {
    return this.addProductForm.get('name');
  }

  get description() {
    return this.addProductForm.get('description');
  }

  get price() {
    return this.addProductForm.get('price');
  }

  goBack(): void {
    this.router.navigateByUrl('/');
  }
}
