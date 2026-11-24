package com.ads.quest_tracker.service;

import com.ads.quest_tracker.model.Priority;
import com.ads.quest_tracker.model.SubTask;
import com.ads.quest_tracker.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes Unitários - SubtaskService")
class SubtaskServiceTest {

    private SubtaskService subtaskService;
    private Task parentTask;

    @BeforeEach
    void setUp() {
        subtaskService = new SubtaskService();
        parentTask = new Task(1L, "Preparar entrega", Priority.HIGH);
    }

    @Test
    @DisplayName("Deve adicionar e listar subtarefas usando a LinkedList da tarefa principal")
    void shouldAddAndListSubtasks() {
        SubTask s1 = new SubTask(1L, "Escrever testes");
        SubTask s2 = new SubTask(2L, "Revisar código", "Checar padrão");

        subtaskService.addSubtask(parentTask, s1);
        subtaskService.addSubtask(parentTask, s2);

        List<SubTask> subtasks = subtaskService.listSubtasks(parentTask);
        assertEquals(List.of(s1, s2), subtasks);
        assertEquals(2, parentTask.getSubtasks().size());
    }

    @Test
    @DisplayName("Deve marcar subtarefa como concluída e consultar subtarefas pendentes")
    void shouldCompleteSubtaskAndListPending() {
        SubTask s1 = new SubTask(1L, "Passo 1");
        SubTask s2 = new SubTask(2L, "Passo 2");
        SubTask s3 = new SubTask(3L, "Passo 3");

        subtaskService.addSubtask(parentTask, s1);
        subtaskService.addSubtask(parentTask, s2);
        subtaskService.addSubtask(parentTask, s3);

        SubTask completed = subtaskService.markSubtaskAsCompleted(parentTask, 2L);
        assertTrue(completed.isCompleted());

        List<SubTask> pending = subtaskService.listPendingSubtasks(parentTask);
        assertEquals(List.of(s1, s3), pending);
    }

    @Test
    @DisplayName("Deve validar duplicidade de ID, nulos e subtarefa inexistente")
    void shouldValidateErrorsInSubtaskOperations() {
        assertThrows(NullPointerException.class, () -> subtaskService.addSubtask(null, new SubTask(1L, "A")));
        assertThrows(NullPointerException.class, () -> subtaskService.addSubtask(parentTask, null));

        subtaskService.addSubtask(parentTask, new SubTask(10L, "Sub 10"));
        assertThrows(IllegalArgumentException.class,
                () -> subtaskService.addSubtask(parentTask, new SubTask(10L, "Duplicada")));

        assertThrows(NoSuchElementException.class,
                () -> subtaskService.markSubtaskAsCompleted(parentTask, 999L));
        assertThrows(IllegalArgumentException.class,
                () -> subtaskService.markSubtaskAsCompleted(parentTask, 0L));
    }
}
