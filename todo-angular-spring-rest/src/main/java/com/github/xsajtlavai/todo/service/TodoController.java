package com.github.xsajtlavai.todo.service;

import com.github.xsajtlavai.todo.domain.Todo;
import com.github.xsajtlavai.todo.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RepositoryRestController
public class TodoController {

    private final TodoRepository todoRepository;
    private final RepositoryEntityLinks entityLinks;

    public TodoController(
            @Autowired TodoRepository todoRepository,
            @Autowired RepositoryEntityLinks entityLinks) {
        this.todoRepository = todoRepository;
        this.entityLinks = entityLinks;
    }

    @RequestMapping(method = GET, value = "/todos/search/usersTodos")
    public @ResponseBody ResponseEntity<?> getUsersTodos() {
        List<Todo> todos = todoRepository.findByUserId(SecurityUtil.getCurrentPrincipal());
        List<Resource<Todo>> todosWithLinks = new ArrayList<>();

        todos.stream()
                .map((todo) -> {
                    Resource<Todo> todoResource = new Resource<>(todo);
                    Link todoLink = entityLinks.linkToSingleResource(Todo.class, todo.getId()).withSelfRel();
                    todoResource.add(todoLink);
                    return todoResource;
                })
                .forEach(todosWithLinks::add);

        Resources<Resource<Todo>> resources = new Resources<>(todosWithLinks);
        resources.add(linkTo(methodOn(TodoController.class).getUsersTodos()).withSelfRel());

        return ResponseEntity.ok(resources);
    }
}
