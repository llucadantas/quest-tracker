
package com.ads.quest_tracker.controller;


import com.ads.quest_tracker.model.Task;
import com.ads.quest_tracker.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Cadastrar tarefa
    @PostMapping
    public ResponseEntity<Task> registerTask(@RequestBody Task task) {
        Task created = taskService.registerTask(task);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    // Listar todas as tarefas
    @GetMapping
    public ResponseEntity<List<Task>> listAllTasks() {
        return ResponseEntity.ok(taskService.listAllTasks());
    }

    // Buscar tarefa por ID
    @GetMapping("/{id}")
    public ResponseEntity<Task> findTaskById(@PathVariable Long id) {
        return taskService.findTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Listar tarefas pendentes
    @GetMapping("/pending")
    public ResponseEntity<List<Task>> listPendingTasks() {
        return ResponseEntity.ok(taskService.listPendingTasks());
    }

    // Remover tarefa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeTask(@PathVariable Long id) {
        boolean removed = taskService.removeTask(id);

        if (!removed) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    // Contar tarefas
    @GetMapping("/count")
    public ResponseEntity<Integer> countTasks() {
        return ResponseEntity.ok(taskService.countTasks());
    }

    // Verificar se está vazio
    @GetMapping("/empty")
    public ResponseEntity<Boolean> isEmpty() {
        return ResponseEntity.ok(taskService.isEmpty());
    }
}
