package com.ads.quest_tracker.service;

import com.ads.quest_tracker.model.Priority;
import com.ads.quest_tracker.model.Task;
import com.ads.quest_tracker.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes Unitários - ExecutionQueueService")
class ExecutionQueueServiceTest {

    private ExecutionQueueService executionQueueService;

    @BeforeEach
    void setUp() {
        executionQueueService = new ExecutionQueueService();
    }

    @Test
    @DisplayName("Deve iniciar sem tarefas aguardando execução e lançar exceção ao consultar ou executar vazia")
    void shouldHandleEmptyQueue() {
        assertFalse(executionQueueService.hasPendingTasks());
        assertEquals(0, executionQueueService.queueSize());
        assertTrue(executionQueueService.listQueuedTasks().isEmpty());
        assertThrows(NoSuchElementException.class, () -> executionQueueService.peekNextTask());
        assertThrows(NoSuchElementException.class, () -> executionQueueService.executeNextTask());
    }

    @Test
    @DisplayName("Deve executar tarefas respeitando a maior prioridade e ordem de chegada em empates")
    void shouldExecuteTasksByPriorityAndTieBreakByFifo() {
        Task low = new Task(1L, "Baixa", Priority.LOW);
        Task highFirst = new Task(2L, "Alta 1", Priority.HIGH);
        Task urgent = new Task(3L, "Urgente", Priority.URGENT);
        Task highSecond = new Task(4L, "Alta 2", Priority.HIGH);

        executionQueueService.enqueueTask(low);
        executionQueueService.enqueueTask(highFirst);
        executionQueueService.enqueueTask(urgent);
        executionQueueService.enqueueTask(highSecond);

        assertTrue(executionQueueService.hasPendingTasks());
        assertEquals(4, executionQueueService.queueSize());
        assertEquals(urgent, executionQueueService.peekNextTask());

        List<Task> queued = executionQueueService.listQueuedTasks();
        assertEquals(List.of(urgent, highFirst, highSecond, low), queued);
        assertEquals(4, executionQueueService.queueSize());

        Task firstExecuted = executionQueueService.executeNextTask();
        assertEquals(urgent, firstExecuted);
        assertEquals(TaskStatus.COMPLETED, firstExecuted.getStatus());

        assertEquals(highFirst, executionQueueService.executeNextTask());
        assertEquals(highSecond, executionQueueService.executeNextTask());
        assertEquals(low, executionQueueService.executeNextTask());
        assertFalse(executionQueueService.hasPendingTasks());
    }

    @Test
    @DisplayName("Deve rejeitar tarefa nula ou já concluída ao enfileirar")
    void shouldRejectNullOrCompletedTaskOnEnqueue() {
        assertThrows(NullPointerException.class, () -> executionQueueService.enqueueTask(null));

        Task completed = new Task(1L, "Finalizada", "Desc", Priority.HIGH, TaskStatus.COMPLETED);
        assertThrows(IllegalArgumentException.class, () -> executionQueueService.enqueueTask(completed));
    }
}
