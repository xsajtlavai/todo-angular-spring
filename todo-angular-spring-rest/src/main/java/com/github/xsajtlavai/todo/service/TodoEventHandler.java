package com.github.xsajtlavai.todo.service;

import com.github.xsajtlavai.todo.domain.Todo;
import com.github.xsajtlavai.todo.security.SecurityUtil;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;

@RepositoryEventHandler(Todo.class)
public class TodoEventHandler extends AbstractRepositoryEventListener {

    @Override
    protected void onBeforeCreate(Object entity) {
        Todo todo = (Todo)entity;
        String currentPrincipal = SecurityUtil.getCurrentPrincipal();
        todo.setUserId(currentPrincipal);
    }

    @Override
    protected void onBeforeSave(Object entity) {
        Todo todo = (Todo)entity;
        String currentPrincipal = SecurityUtil.getCurrentPrincipal();
        todo.setUserId(currentPrincipal);
    }


}
