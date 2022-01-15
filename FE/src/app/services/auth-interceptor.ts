import {
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { exhaustMap, take } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { LoginService } from './login.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  urlsToNotUse: Array<string>= [
      'authenticate',
      'registration',
    ];

  constructor(private logService: LoginService) {}

   intercept(req: HttpRequest<any>, next: HttpHandler) {

    if (this.isValidRequestForInterceptor(req.url)) {
        let user=this.logService.getLoggedUser();
        let token= this.logService.getToken();
        if (!user) {
          return next.handle(req);
        }
        const modifiedReq: HttpRequest<any> = req.clone({
          headers: req.headers.set('Authorization', 'Bearer ' + token),
        });
        return next.handle(modifiedReq);
   }
   return next.handle(req);
  }

  private isValidRequestForInterceptor(requestUrl: string): boolean {
    let positionIndicator: string = `${environment.api}/`;
    let position = requestUrl.indexOf(positionIndicator);
    if (position > 0) {
      let destination: string = requestUrl.substr(position + positionIndicator.length);
      for (let address of this.urlsToNotUse) {
        if (new RegExp(address).test(destination)) {
          return false;
        }
      }
    }
    return true;
  }
}
