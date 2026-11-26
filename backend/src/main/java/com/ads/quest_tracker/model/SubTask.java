package com.ads.quest_tracker.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class SubTask {
    private final Long id;
    private String title;
    private String description;
    private boolean completed;

    public SubTask(Long id, String title) {
        this(id, title, "");
    }

    @JsonCreator
    public SubTask(
            @JsonProperty("id") Long id,
            @JsonProperty("title") String title,
            @JsonProperty("description") String description
    ) {
        this.id = validateId(id);
        this.title = validateTitle(title);
        this.description = description != null ? description : "";
        this.completed = false;
    }

    private static Long validateId(Long id) {
        Objects.requireNonNull(id, "O identificador da subtarefa não pode ser nulo.");
        if (id <= 0) {
            throw new IllegalArgumentException("O identificador da subtarefa deve ser positivo.");
        }
        return id;
    }

    private static String validateTitle(String title) {
        Objects.requireNonNull(title, "O título da subtarefa não pode ser nulo.");
        if (title.isBlank()) {
            throw new IllegalArgumentException("O título da subtarefa não pode estar em branco.");
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

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void markAsCompleted() {
        this.completed = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubTask subTask)) {
            return false;
        }
        return Objects.equals(id, subTask.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", completed=" + completed +
                '}';
    }
}
