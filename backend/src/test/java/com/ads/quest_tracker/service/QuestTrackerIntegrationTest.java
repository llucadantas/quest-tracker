package com.ads.quest_tracker.service;

import com.ads.quest_tracker.model.Priority;
import com.ads.quest_tracker.model.SubTask;
import com.ads.quest_tracker.model.Task;
import com.ads.quest_tracker.model.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de Integração - Fluxo Completo de Services e Estruturas")
class QuestTrackerIntegrationTest {
    @Test
    @DisplayName("Deve integrar TaskService (AVLTree), ExecutionQueueService (PriorityQueue) e SubtaskService (LinkedList)")
    void shouldIntegrateAllServicesAndDataStructures() {
        TaskService taskService = new TaskService();
        ExecutionQueueService executionQueueService = new ExecutionQueueService();
        SubtaskService subtaskService = new SubtaskService();

        Task t1 = new Task(30L, "Criar documentação", Priority.LOW);
        Task t2 = new Task(10L, "Corrigir bug crítico", Priority.URGENT);
        Task t3 = new Task(20L, "Implementar endpoints", Priority.HIGH);

        taskService.registerTask(t1);
        taskService.registerTask(t2);
        taskService.registerTask(t3);

        Task foundTask = taskService.findTaskById(20L).orElseThrow();
        subtaskService.addSubtask(foundTask, new SubTask(1L, "Criar controller"));
        subtaskService.addSubtask(foundTask, new SubTask(2L, "Validar DTO"));
        subtaskService.markSubtaskAsCompleted(foundTask, 1L);

        assertEquals(2, subtaskService.listSubtasks(foundTask).size());
        assertEquals(1, subtaskService.listPendingSubtasks(foundTask).size());

        executionQueueService.enqueueTask(t1);
        executionQueueService.enqueueTask(t2);
        executionQueueService.enqueueTask(t3);

        assertEquals(List.of(t2, t3, t1), executionQueueService.listQueuedTasks());

        Task executedFirst = executionQueueService.executeNextTask();
        assertEquals(t2, executedFirst);
        assertEquals(TaskStatus.COMPLETED, taskService.findTaskById(10L).orElseThrow().getStatus());

        assertEquals(List.of(t3, t1), taskService.listPendingTasks());

        assertTrue(taskService.removeTask(10L));
        assertEquals(2, taskService.countTasks());
    }
}
