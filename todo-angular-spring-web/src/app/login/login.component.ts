import {Component, OnInit} from '@angular/core';
import {LoginService} from "../login.service";
import {Router} from "@angular/router";
import { Observable } from "rxjs/Rx"

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  username: string;
  password: string;

  warning: string;
  submitButtonDisabled: boolean = false;

  constructor(
    private loginService: LoginService,
    private router: Router
  ) { }

  ngOnInit() {
  }

  login() {
    this.doBeforeSubmit();

    this.loginService.login(this.username, this.password).subscribe(loginStatus => {
      this.doAfterResponse();

      if (loginStatus) {
        this.router.navigate(['/todos']);
      } else {
        this.warning = 'Login failed.';
      }
    });
  }

  private doBeforeSubmit() {
    this.warning = null;
    this.submitButtonDisabled = true;
  }

  private doAfterResponse() {
    this.submitButtonDisabled = false
  }
}
