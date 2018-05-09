package com.github.xsajtlavai.todo.service;

import com.github.xsajtlavai.todo.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "todos", path = "todos")
public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByUserId(String userId);

    List<Todo> findByIdAndUserId(Long id, String userId);

}
