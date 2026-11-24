package com.ads.quest_tracker.service;

import com.ads.quest_tracker.model.Priority;
import com.ads.quest_tracker.model.Task;
import com.ads.quest_tracker.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes Unitários - TaskService")
class TaskServiceTest {
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Test
    @DisplayName("Deve cadastrar e buscar tarefa pelo identificador usando AVLTree")
    void shouldRegisterAndFindTaskById() {
        Task task = new Task(10L, "Implementar Heap", "Criar PriorityQueue", Priority.HIGH);

        Task registered = taskService.registerTask(task);

        assertEquals(task, registered);
        assertEquals(1, taskService.countTasks());
        assertFalse(taskService.isEmpty());

        Optional<Task> found = taskService.findTaskById(10L);
        assertTrue(found.isPresent());
        assertEquals("Implementar Heap", found.get().getTitle());
        assertTrue(taskService.findTaskById(999L).isEmpty());
    }

    @Test
    @DisplayName("Deve rejeitar cadastro de tarefa nula ou com identificador duplicado")
    void shouldRejectNullOrDuplicateTask() {
        assertThrows(NullPointerException.class, () -> taskService.registerTask(null));

        taskService.registerTask(new Task(1L, "Tarefa 1", Priority.LOW));
        assertThrows(IllegalArgumentException.class,
                () -> taskService.registerTask(new Task(1L, "Duplicada", Priority.HIGH)));
    }

    @Test
    @DisplayName("Deve validar identificador inválido na busca e remoção")
    void shouldValidateInvalidIds() {
        assertThrows(NullPointerException.class, () -> taskService.findTaskById(null));
        assertThrows(IllegalArgumentException.class, () -> taskService.findTaskById(0L));
        assertThrows(IllegalArgumentException.class, () -> taskService.removeTask(-5L));
    }

    @Test
    @DisplayName("Deve remover tarefa pelo identificador")
    void shouldRemoveTaskById() {
        taskService.registerTask(new Task(5L, "T5", Priority.MEDIUM));
        taskService.registerTask(new Task(2L, "T2", Priority.LOW));

        assertTrue(taskService.removeTask(5L));
        assertFalse(taskService.removeTask(5L));
        assertEquals(1, taskService.countTasks());
        assertTrue(taskService.findTaskById(5L).isEmpty());
        assertTrue(taskService.findTaskById(2L).isPresent());
    }

    @Test
    @DisplayName("Deve listar todas as tarefas e filtrar apenas as pendentes")
    void shouldListAllAndPendingTasks() {
        Task t1 = new Task(3L, "T3", Priority.LOW);
        Task t2 = new Task(1L, "T1", "Desc", Priority.HIGH, TaskStatus.COMPLETED);
        Task t3 = new Task(2L, "T2", "Desc", Priority.URGENT, TaskStatus.IN_PROGRESS);
        Task t4 = new Task(4L, "T4", Priority.MEDIUM);

        taskService.registerTask(t1);
        taskService.registerTask(t2);
        taskService.registerTask(t3);
        taskService.registerTask(t4);

        List<Task> all = taskService.listAllTasks();
        assertEquals(List.of(t2, t3, t1, t4), all);

        List<Task> pending = taskService.listPendingTasks();
        assertEquals(List.of(t1, t4), pending);
    }
}
