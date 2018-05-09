package com.github.xsajtlavai.todo.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class RestServiceConfig extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
        validatingListener.addValidator("beforeSave", beforeSaveTodoValidator());
        validatingListener.addValidator("beforeDelete", beforeDeleteTodoValidator());
        super.configureValidatingRepositoryEventListener(validatingListener);
    }

    @Bean
    public TodoEventHandler todoEventHandler() {
        return new TodoEventHandler();
    }

    @Bean
    public TodoValidator beforeSaveTodoValidator() {
        return new TodoValidator();
    }

    @Bean
    public TodoValidator beforeDeleteTodoValidator() {
        return new TodoValidator();
    }

}
