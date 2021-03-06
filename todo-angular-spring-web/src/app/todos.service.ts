import {Injectable} from '@angular/core';
import {Observable} from "rxjs/Observable";
import {Todo} from "./todo";
import {HttpClient} from "@angular/common/http";
import {environment} from "../environments/environment";

@Injectable()
export class TodosService {

  constructor(
    private http: HttpClient
  ) { }

  save(todo: Todo): Observable<Todo> {
    const posturl = `${environment.restapiurl}/todos`;
    return this.http.post<Todo>(posturl, todo);
  }

  delete(todo: Todo) {
    const deleteurl = todo._links.self.href;
    return this.http.delete<Todo>(deleteurl);
  }

  merge(todo: Todo): Observable<Todo> {
    const puturl = todo._links.self.href;
    return this.http.put<Todo>(puturl, todo);
  }

  getAllUserTodos() {
    const geturl = `${environment.restapiurl}/todos/search/usersTodos`;
    return this.http.get<Todo[]>(geturl).switchMap((response: any) => {
      if (response && response._embedded && response._embedded.todos) {
        return Observable.of<Todo[]>(response._embedded.todos)
      }
      return Observable.of<Todo[]>([ ]);
    });
  }
}
