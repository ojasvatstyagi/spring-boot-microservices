import {Component, inject, OnInit} from '@angular/core';
import {OidcSecurityService} from "angular-auth-oidc-client";
import {NgIf, NgClass} from "@angular/common";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [NgIf, NgClass, RouterLink],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {

  private readonly oidcSecurityService = inject(OidcSecurityService);
  isAuthenticated = false;
  username = "";
  showMobileMenu = false;

  ngOnInit(): void {
    this.oidcSecurityService.isAuthenticated$.subscribe(
      ({isAuthenticated}) => {
        this.isAuthenticated = isAuthenticated;
        // If not authenticated, ensure username is cleared immediately
        if (!isAuthenticated) {
          this.username = "";
        }
      }
    );

    // IMPORTANT FIX: Add null check for userData
    this.oidcSecurityService.userData$.subscribe(
      ({userData}) => {
        if (userData) { // Check if userData is not null or undefined
          this.username = userData.preferred_username || userData.name || userData.email || "";
        } else {
          this.username = ""; // Clear username if userData is null/undefined
        }
      }
    );
  }

  login(): void {
    this.oidcSecurityService.authorize();
  }

  logout(): void {
    this.oidcSecurityService
      .logoff()
      .subscribe((result) => console.log('Logout result:', result));
  }

  toggleMobileMenu(): void {
    this.showMobileMenu = !this.showMobileMenu;
  }
}
