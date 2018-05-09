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

import com.github.xsajtlavai.todo.security.jwt.JwtUtil;
import com.github.xsajtlavai.todo.service.TodoRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

}
