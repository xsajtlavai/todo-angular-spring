import {Injectable} from '@angular/core';
import {User} from "./user";
import {Observable} from "rxjs/Rx"
import {HttpClient, HttpErrorResponse, HttpParams, HttpResponse} from "@angular/common/http";

import {environment} from '../environments/environment';

@Injectable()
export class LoginService {

  private loggedUser: User;
  private jwtToken: string;

  constructor(
    private http: HttpClient,
  ) { }

  getLoggedUser() {
    return this.loggedUser;
  }

  isUserLoggedIn(): boolean {
    return !!this.loggedUser;
  }

  login(username: string, password: string): Observable<boolean> {
    const loginurl = `${environment.restapiurl}/login`;

    let httpParams = new HttpParams()
      .append("username", username)
      .append("password", password);

    return this.http.post(loginurl, <string> null,
      { observe: 'response', params: httpParams, responseType: 'text' })
      .switchMap((response: HttpResponse<string>) => {
        this.jwtToken = response.headers.get('Authorization');
        if (this.jwtToken) {
          this.loggedUser = new User(username);
          return Observable.of<boolean>(true);
        }
        return Observable.of<boolean>(false);
      }).catch((error: HttpErrorResponse) => {
        console.log(`${error.status} - ${error.message}`);
        return Observable.of<boolean>(false);
      });
  }

  getJwtToken(): string {
    return this.jwtToken;
  }

  removeJwtToken() {
    this.jwtToken = null;
  }
}
