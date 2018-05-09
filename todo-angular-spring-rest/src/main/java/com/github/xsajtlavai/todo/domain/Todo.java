package com.github.xsajtlavai.todo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Todo implements Serializable {

    private static final long serialVersionUID = 1L;

    public Todo() {
    }

    public Todo(String task, Boolean active) {
        this.task = task;
        this.active = active;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String task;

    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return Objects.equals(id, todo.id) &&
                Objects.equals(task, todo.task) &&
                Objects.equals(active, todo.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, task, active);
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", task='" + task + '\'' +
                ", active=" + active +
                '}';
    }
}
