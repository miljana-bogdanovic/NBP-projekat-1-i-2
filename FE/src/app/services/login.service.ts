import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { LoggedUser } from '../models/logged-user.model';
import { User } from '../models/user.model';
import { AuthService } from './auth.service';
import { UserService } from './user.service';

@Injectable({ providedIn: 'root' })
export class LoginService {
  loggedUser: Subject<LoggedUser | null> = new Subject<LoggedUser | null>();

  private tokenExpTimer: any;

  constructor(
    private router: Router,
    private http: HttpClient,
    private authService: AuthService,

  ) {}

  getLoggedUser(){
    //localStorage.setItem('user', 'miljana');
   return localStorage.getItem('user') || '';
  }

  getToken(){
    return localStorage.getItem('token') || '';
  }
  loginUser(username: string, password: string) {
    return this.http
      .post<{
        username: string;
        token: string;
      }>(`${environment.api}/authenticate`, {
        username: username,
        password: password,
      })
      .pipe(
        tap((loggingData) => {
          if (loggingData.token !== null) {
            const loggedUser: LoggedUser = new LoggedUser(
             username,
              loggingData.token,
             // date
           );
           this.loggedUser.next(loggedUser);
            localStorage.setItem('user', loggedUser.username);
            localStorage.setItem('token', loggedUser.Token);
           // this.autoLogout(loggingData.tokenExpiration * 60 * 1000);
            this.router.navigateByUrl('/home');
          }
        })
      );
  }



  logout() {
    this.authService.logout();
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }


}
