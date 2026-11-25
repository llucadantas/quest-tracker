
package com.ads.quest_tracker.controller;

import com.ads.quest_tracker.model.Task;
import com.ads.quest_tracker.service.ExecutionQueueService;
import com.ads.quest_tracker.service.TaskService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/execution-queue")
public class ExecutionQueueController {

    private final ExecutionQueueService executionQueueService;
    private final TaskService taskService;

    public ExecutionQueueController(
            ExecutionQueueService executionQueueService,
            TaskService taskService
    ) {
        this.executionQueueService = executionQueueService;
        this.taskService = taskService;
    }

    // Adicionar tarefa à fila
    @PostMapping("/{taskId}")
    public ResponseEntity<Void> enqueueTask(
            @PathVariable Long taskId
    ) {
        Task task = findTaskOrThrow(taskId);

        executionQueueService.enqueueTask(task);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Consultar próxima tarefa
    @GetMapping("/next")
    public ResponseEntity<Task> peekNextTask() {
        Task nextTask = executionQueueService.peekNextTask();

        if (nextTask == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(nextTask);
    }

    // Executar próxima tarefa
    @PostMapping("/execute")
    public ResponseEntity<Task> executeNextTask() {
        if (!executionQueueService.hasPendingTasks()) {
            return ResponseEntity.noContent().build();
        }

        Task executed = executionQueueService.executeNextTask();

        return ResponseEntity.ok(executed);
    }

    // Listar tarefas na fila
    @GetMapping
    public ResponseEntity<List<Task>> listQueuedTasks() {
        return ResponseEntity.ok(
                executionQueueService.listQueuedTasks()
        );
    }

    // Consultar quantidade de tarefas
    @GetMapping("/size")
    public ResponseEntity<Integer> queueSize() {
        return ResponseEntity.ok(
                executionQueueService.queueSize()
        );
    }

    // Verificar se existem tarefas pendentes
    @GetMapping("/pending")
    public ResponseEntity<Boolean> hasPendingTasks() {
        return ResponseEntity.ok(
                executionQueueService.hasPendingTasks()
        );
    }

    private Task findTaskOrThrow(Long taskId) {
        return taskService.findTaskById(taskId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Tarefa com identificador " + taskId + " não encontrada."
                ));
    }
}
