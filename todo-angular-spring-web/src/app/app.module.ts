import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {BsDropdownModule, ModalModule, TooltipModule} from "ngx-bootstrap";
import {LoginComponent} from './login/login.component';
import {TodosComponent} from './todos/todos.component';
import {AppRoutingModule} from './app-routing.module';
import {LoginService} from "./login.service";
import {AuthGuard} from "./auth.guard";
import {FormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {JwsTokenInterceptor} from "./jws-token.interceptor";
import {TodosService} from "./todos.service";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    TodosComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    BsDropdownModule.forRoot(),
    TooltipModule.forRoot(),
    ModalModule.forRoot()
  ],
  providers: [
    LoginService,
    TodosService,
    AuthGuard,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwsTokenInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
