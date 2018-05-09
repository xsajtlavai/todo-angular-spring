/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.xsajtlavai.todo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.xsajtlavai.todo.domain.Todo;
import com.github.xsajtlavai.todo.security.jwt.JwtUtil;
import com.github.xsajtlavai.todo.service.TodoRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class TodoServiceTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private TodoRepository todoRepository;

	@Before
	public void cleanDataBeforeTests() {
		this.todoRepository.deleteAll();
	}

	@Test
	public void failedGetEmptyTodosWithoutAuth() throws Exception {
		this.mvc.perform(get("/todos"))
				.andExpect(status().isForbidden());
	}

	@Test
	public void loginAndGetJwtToken() throws Exception {
		this.mvc.perform(post("/login")
				.param("username", "user1")
				.param("password", "password1"))
		.andExpect(status().isOk())
		.andExpect(header().exists("Authorization"));
	}

	@Test
	public void loginWithBadPassword() throws Exception {
		this.mvc.perform(post("/login")
				.param("username", "user1")
				.param("password", "badPassword"))
				.andExpect(status().isUnauthorized())
				.andExpect(header().doesNotExist("Authorization"));
	}

	@Test
	public void getEmptyTodosWithJwtToken() throws Exception {
		String tokenWithHeaderPrefix = JwtUtil.createTokenWithHeaderPrefix("user1");

		this.mvc.perform(get("/todos")
				.header(HttpHeaders.AUTHORIZATION, tokenWithHeaderPrefix))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.todos", hasSize(0)));
	}

	@Test
	public void deleteTodoWithJwtToken() throws Exception {
		String tokenWithHeaderPrefix = JwtUtil.createTokenWithHeaderPrefix("user1");
		Todo newTodo = this.todoRepository.save(new Todo("test", true, "user1"));

		this.mvc.perform(delete("/todos/" + newTodo.getId())
				.header(HttpHeaders.AUTHORIZATION, tokenWithHeaderPrefix))
				.andExpect(status().isNoContent());
	}

	@Test
	public void putTodoWithJwtToken() throws Exception {
		String tokenWithHeaderPrefix = JwtUtil.createTokenWithHeaderPrefix("user1");
		Todo newTodo = this.todoRepository.save(new Todo("test", true, "user1"));

		this.mvc.perform(put("/todos/" + newTodo.getId())
				.header(HttpHeaders.AUTHORIZATION, tokenWithHeaderPrefix)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(toJson(newTodo)))
				.andExpect(status().isNoContent());

		Optional<Todo> todoOptional = this.todoRepository.findById(newTodo.getId());
		Assert.assertTrue(todoOptional.isPresent());
		Assert.assertEquals("user1", todoOptional.get().getUserId());
	}

	@Test
	public void searchUsersTodos() throws Exception {
		String tokenWithHeaderPrefix = JwtUtil.createTokenWithHeaderPrefix("user1");
		Todo todo = this.todoRepository.save(new Todo("test1", true, "user1"));
		this.todoRepository.save(new Todo("test2", true, "user2"));

		this.mvc.perform(get("/todos/search/usersTodos")
				.header(HttpHeaders.AUTHORIZATION, tokenWithHeaderPrefix))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.todos", hasSize(1)))
				.andExpect(jsonPath("$._embedded.todos[0].task", equalTo(todo.getTask())))
				.andExpect(jsonPath("$._embedded.todos[0]._links.self.href", endsWith("/todos/" + todo.getId())));
	}

	private String toJson(Todo todo) {
		try {
			ObjectMapper jsonMapper = new ObjectMapper();
			return jsonMapper.writeValueAsString(new TodoDTO(todo));
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	private static class TodoDTO {
		private String task;
		private boolean active;
		private String _links;

		private TodoDTO(Todo todo) {
			this.task = todo.getTask();
			this.active = todo.getActive();
			this._links = "http://localhost:8080/todos/?";
		}

		public String getTask() {
			return task;
		}

		public void setTask(String task) {
			this.task = task;
		}

		public boolean isActive() {
			return active;
		}

		public void setActive(boolean active) {
			this.active = active;
		}

		public String get_links() {
			return _links;
		}

		public void set_links(String _links) {
			this._links = _links;
		}
	}

}
