
package com.ads.quest_tracker.controller;

import com.ads.quest_tracker.model.SubTask;
import com.ads.quest_tracker.model.Task;
import com.ads.quest_tracker.service.SubtaskService;
import com.ads.quest_tracker.service.TaskService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/tasks/{taskId}/subtasks")
public class SubtaskController {

    private final SubtaskService subtaskService;
    private final TaskService taskService;

    public SubtaskController(
            SubtaskService subtaskService,
            TaskService taskService
    ) {
        this.subtaskService = subtaskService;
        this.taskService = taskService;
    }

    // Adicionar subtarefa
    @PostMapping
    public ResponseEntity<SubTask> addSubtask(
            @PathVariable Long taskId,
            @RequestBody SubTask subtask
    ) {
        Task task = findTaskOrThrow(taskId);

        SubTask created = subtaskService.addSubtask(task, subtask);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    // Listar todas as subtarefas
    @GetMapping
    public ResponseEntity<List<SubTask>> listSubtasks(
            @PathVariable Long taskId
    ) {
        Task task = findTaskOrThrow(taskId);

        return ResponseEntity.ok(
                subtaskService.listSubtasks(task)
        );
    }

    // Listar subtarefas pendentes
    @GetMapping("/pending")
    public ResponseEntity<List<SubTask>> listPendingSubtasks(
            @PathVariable Long taskId
    ) {
        Task task = findTaskOrThrow(taskId);

        return ResponseEntity.ok(
                subtaskService.listPendingSubtasks(task)
        );
    }

    // Marcar subtarefa como concluída
    @PatchMapping("/{subtaskId}/complete")
    public ResponseEntity<SubTask> markAsCompleted(
            @PathVariable Long taskId,
            @PathVariable Long subtaskId
    ) {
        Task task = findTaskOrThrow(taskId);

        SubTask completed = subtaskService.markSubtaskAsCompleted(
                task,
                subtaskId
        );

        return ResponseEntity.ok(completed);
    }

    // Buscar tarefa principal
    private Task findTaskOrThrow(Long taskId) {
        return taskService.findTaskById(taskId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Tarefa com identificador " + taskId + " não encontrada."
                ));
    }
}
