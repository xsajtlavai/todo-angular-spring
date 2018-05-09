import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {LoginService} from "./login.service";
import {Router} from "@angular/router";

@Injectable()
export class JwsTokenInterceptor implements HttpInterceptor {

  constructor(
    private loginService: LoginService,
    private router: Router
  ) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const jwtToken = this.loginService.getJwtToken();
    if (jwtToken) {
      request = request.clone({
        setHeaders: {
          Authorization: this.loginService.getJwtToken()
        }
      });
    }

    return next.handle(request).do((event: HttpEvent<any>) => {
    }, (err: any) => {
      if (err instanceof HttpErrorResponse) {
        if (err.status === 401 || err.status === 403) {
          this.loginService.removeJwtToken();
          this.router.navigate(['/login']);
        }
      }
    });
  }
}
