import { Component, OnInit } from '@angular/core';
import {TodosService} from "../todos.service";
import {Todo} from "../todo";

@Component({
  selector: 'app-todos',
  templateUrl: './todos.component.html',
  styleUrls: ['./todos.component.css']
})
export class TodosComponent implements OnInit {

  newTodo: string;
  todos: Todo[] = [ ];

  constructor(
    private todosService: TodosService
  ) { }

  ngOnInit() {
  }

  addTodo() {
    if (this.newTodo) {
      this.todosService.save(new Todo(this.newTodo, true)).subscribe(newTodo => {
        if (newTodo && newTodo._links) {
          this.newTodo = null;
          this.todos = [...this.todos, newTodo];
        }
      });
    }
  }

  deleteTodo(todo: Todo) {
    this.todosService.delete(todo).subscribe(() => {
      this.todos = this.todos.filter((todoItem) => todoItem._links.self.href !== todo._links.self.href);
    });
  }

  changeTodoActiveStatus(todo: Todo) {
    const changedTodo = {...todo, active: !todo.active};

    this.todosService.merge(changedTodo).subscribe(changedTodo => {
      const changeItemIndex = this.todos.findIndex(todoItem => todoItem._links.self.href === changedTodo._links.self.href);
      if (changeItemIndex >= 0) {
        this.todos[changeItemIndex] = changedTodo;
        this.todos = [...this.todos];
      }
    });
  }

  deleteAllInactiveTodos() {
    for (let todo of this.todos) {
      if (!todo.active) {
        this.deleteTodo(todo);
      }
    }
  }
}
