import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { from } from 'rxjs';
import { switchMap } from 'rxjs/operators';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
const oidcSecurityService = inject(OidcSecurityService);

return from(oidcSecurityService.getAccessToken()).pipe(
    switchMap((token) => {
      if (token) {
        const clonedReq = req.clone({
          headers: req.headers.set('Authorization', `Bearer ${token}`)
        });
        return next(clonedReq);
      }
      return next(req);
    })
  );
};
