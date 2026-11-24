package com.ads.quest_tracker.service;

import com.ads.quest_tracker.model.Priority;
import com.ads.quest_tracker.model.Task;
import com.ads.quest_tracker.structures.avl.AVLTree;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TaskService {
    private final AVLTree<Task> tasksTree;

    public TaskService() {
        this.tasksTree = new AVLTree<>(Comparator.comparing(Task::getId));
    }

    public Task registerTask(Task task) {
        Objects.requireNonNull(task, "A tarefa não pode ser nula.");
        boolean inserted = tasksTree.insert(task);
        if (!inserted) {
            throw new IllegalArgumentException(
                    "Já existe uma tarefa cadastrada com o identificador: " + task.getId()
            );
        }
        return task;
    }

    public Optional<Task> findTaskById(Long id) {
        Task found = tasksTree.search(createLookupProbe(id));
        return Optional.ofNullable(found);
    }

    public boolean removeTask(Long id) {
        return tasksTree.remove(createLookupProbe(id));
    }

    public List<Task> listAllTasks() {
        return tasksTree.inOrder();
    }

    public List<Task> listPendingTasks() {
        List<Task> pending = new ArrayList<>();
        for (Task task : tasksTree.inOrder()) {
            if (task.isPending()) {
                pending.add(task);
            }
        }
        return Collections.unmodifiableList(pending);
    }

    public int countTasks() {
        return tasksTree.size();
    }

    public boolean isEmpty() {
        return tasksTree.isEmpty();
    }

    private Task createLookupProbe(Long id) {
        return new Task(id, "__lookup__", Priority.LOW);
    }
}
