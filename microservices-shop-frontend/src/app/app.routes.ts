import {Routes} from '@angular/router';
import {HomePageComponent} from "./pages/home-page/home-page.component";
import {AddProductComponent} from "./pages/add-product/add-product.component";

export const routes: Routes = [
  {path: '', component: HomePageComponent},
  // Route for adding a new product
  {path: 'add-product', component: AddProductComponent},
  // Route for editing an existing product, with an optional 'id' parameter
  {path: 'edit-product/:id', component: AddProductComponent} // <--- ADD THIS LINE
];
