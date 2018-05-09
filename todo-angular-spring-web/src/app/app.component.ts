import { Component } from '@angular/core';
import {LoginService} from "./login.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor(
    private loginService : LoginService
  ) {}

  get userName() {
    const loggedUser = this.loginService.getLoggedUser();
    return loggedUser ? loggedUser.username : null;
  }

}
