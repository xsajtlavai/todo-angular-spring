package com.github.xsajtlavai.todo.service;

import com.github.xsajtlavai.todo.domain.Todo;
import com.github.xsajtlavai.todo.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RepositoryRestController
public class TodoController {

    private TodoRepository todoRepository;

    public TodoController(@Autowired TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @RequestMapping(method = GET, value = "/todos/search/usersTodos")
    public @ResponseBody ResponseEntity<?> getUsersTodos() {
        List<Todo> todos = todoRepository.findByUserId(SecurityUtil.getCurrentPrincipal());
        Resources<Todo> resources = new Resources<>(todos);
        resources.add(linkTo(methodOn(TodoController.class).getUsersTodos()).withSelfRel());

        return ResponseEntity.ok(resources);
    }
}
