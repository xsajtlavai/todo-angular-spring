package com.github.xsajtlavai.todo.service;

import com.github.xsajtlavai.todo.domain.Todo;
import com.github.xsajtlavai.todo.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

public class TodoValidator implements Validator {

    @Autowired
    private TodoRepository todoRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return Todo.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object todoObject, Errors errors) {
        if (!isCurrentPrincipalOwner((Todo) todoObject)) {
            errors.reject("Current principal is not owner of todo");
        }
    }

    private boolean isCurrentPrincipalOwner(Todo todo) {
        String currentPrincipal = SecurityUtil.getCurrentPrincipal();
        List<Todo> todos = todoRepository.findByIdAndUserId(todo.getId(), currentPrincipal);
        return !todos.isEmpty();
    }
}
