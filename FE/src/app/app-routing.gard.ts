import { Route } from '@angular/compiler/src/core';
import { Injectable, ɵConsole } from '@angular/core';
import { ActivatedRoute, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from './services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard {
  constructor(
    private router: Router,
    private authService: AuthService
) { }

canActivate(route: ActivatedRouteSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    if (!this.authService.isAuthorized()) {
        this.router.navigate(['/login']);
        return false;
    }
    return true;
}

canLoad(route: ActivatedRouteSnapshot): boolean {
    if (!this.authService.isAuthorized()) {
        return false;
    }
    return true;
}
}
