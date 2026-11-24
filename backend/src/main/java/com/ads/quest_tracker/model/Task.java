package com.ads.quest_tracker.model;

import com.ads.quest_tracker.structures.linkedlist.LinkedList;

import java.util.Objects;

public class Task {
    private final Long id;
    private String title;
    private String description;
    private Priority priority;
    private TaskStatus status;
    private final LinkedList<SubTask> subtasks;

    public Task(Long id, String title, Priority priority) {
        this(id, title, "", priority, TaskStatus.PENDING);
    }

    public Task(Long id, String title, String description, Priority priority) {
        this(id, title, description, priority, TaskStatus.PENDING);
    }

    public Task(Long id, String title, String description, Priority priority, TaskStatus status) {
        this.id = validateId(id);
        this.title = validateTitle(title);
        this.description = description != null ? description : "";
        this.priority = Objects.requireNonNull(priority, "A prioridade da tarefa não pode ser nula.");
        this.status = Objects.requireNonNull(status, "O status da tarefa não pode ser nulo.");
        this.subtasks = new LinkedList<>();
    }

    private static Long validateId(Long id) {
        Objects.requireNonNull(id, "O identificador da tarefa não pode ser nulo.");
        if (id <= 0) {
            throw new IllegalArgumentException("O identificador da tarefa deve ser positivo.");
        }
        return id;
    }

    private static String validateTitle(String title) {
        Objects.requireNonNull(title, "O título da tarefa não pode ser nulo.");
        if (title.isBlank()) {
            throw new IllegalArgumentException("O título da tarefa não pode estar em branco.");
        }
        return title.trim();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = validateTitle(title);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description != null ? description : "";
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = Objects.requireNonNull(priority, "A prioridade da tarefa não pode ser nula.");
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = Objects.requireNonNull(status, "O status da tarefa não pode ser nulo.");
    }

    public LinkedList<SubTask> getSubtasks() {
        return subtasks;
    }

    public boolean isPending() {
        return this.status == TaskStatus.PENDING;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task task)) {
            return false;
        }
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", priority=" + priority +
                ", status=" + status +
                ", subtasksCount=" + subtasks.size() +
                '}';
    }
}
