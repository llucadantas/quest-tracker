package com.ads.quest_tracker.structures.priorityqueue;

import com.ads.quest_tracker.model.Priority;
import com.ads.quest_tracker.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes Unitários - PriorityQueue")
class PriorityQueueTest {
    private PriorityQueue<Integer> queue;

    @BeforeEach
    void setUp() {
        queue = new PriorityQueue<>();
    }

    @Nested
    @DisplayName("Inicialização e Estado Vazio")
    class InitializationAndEmptyTests {
        @Test
        @DisplayName("Fila recém-criada deve estar vazia com tamanho zero")
        void shouldInitializeEmptyQueue() {
            assertTrue(queue.isEmpty());
            assertEquals(0, queue.size());
            assertTrue(queue.list().isEmpty());
            assertEquals("[]", queue.toString());
            assertTrue(queue.isValidMaxHeap());
        }

        @Test
        @DisplayName("peek e remove em fila vazia devem lançar NoSuchElementException")
        void shouldThrowNoSuchElementExceptionWhenEmpty() {
            assertThrows(NoSuchElementException.class, () -> queue.peek());
            assertThrows(NoSuchElementException.class, () -> queue.remove());
        }

        @Test
        @DisplayName("Deve rejeitar Comparator nulo e elementos nulos")
        void shouldRejectNullComparatorAndNullElements() {
            assertThrows(NullPointerException.class, () -> new PriorityQueue<Integer>(null));
            assertThrows(NullPointerException.class, () -> queue.insert(null));
        }

        @Test
        @DisplayName("Deve lançar ClassCastException se elemento não implementar Comparable e não houver Comparator")
        void shouldThrowExceptionWhenNotComparable() {
            class NonComparableItem {}
            PriorityQueue<NonComparableItem> invalidQueue = new PriorityQueue<>();
            assertThrows(ClassCastException.class, () -> invalidQueue.insert(new NonComparableItem()));
        }
    }

    @Nested
    @DisplayName("Operações Fundamentais de Max-Heap")
    class CoreHeapOperationsTests {
        @Test
        @DisplayName("Deve manter o elemento de maior prioridade na raiz e consultar sem remover via peek")
        void shouldMaintainMaxAtRootAndPeekWithoutRemoving() {
            queue.insert(10);
            assertEquals(10, queue.peek());

            queue.insert(50);
            assertEquals(50, queue.peek());

            queue.insert(30);
            assertEquals(50, queue.peek());
            assertEquals(3, queue.size());
            assertTrue(queue.isValidMaxHeap());

            assertEquals(50, queue.peek());
            assertEquals(3, queue.size());
        }

        @Test
        @DisplayName("Deve remover elementos sempre em ordem decrescente de prioridade preservando o Heap")
        void shouldRemoveInDescendingPriorityOrder() {
            int[] values = {15, 42, 7, 99, 23, 64, 3, 81};
            for (int value : values) {
                queue.insert(value);
                assertTrue(queue.isValidMaxHeap());
            }

            List<Integer> removed = new ArrayList<>();
            while (!queue.isEmpty()) {
                removed.add(queue.remove());
                assertTrue(queue.isValidMaxHeap());
            }

            assertEquals(List.of(99, 81, 64, 42, 23, 15, 7, 3), removed);
            assertTrue(queue.isEmpty());
            assertEquals(0, queue.size());
        }

        @Test
        @DisplayName("Deve funcionar com Comparator customizado para tarefas e respeitar prioridade")
        void shouldOrderTasksByPriorityWithComparator() {
            PriorityQueue<Task> taskQueue = new PriorityQueue<>(
                    Comparator.comparingInt(task -> task.getPriority().getLevel())
            );

            Task low = new Task(1L, "Documentar", Priority.LOW);
            Task urgent = new Task(2L, "Corrigir crash", Priority.URGENT);
            Task medium = new Task(3L, "Refatorar", Priority.MEDIUM);
            Task high = new Task(4L, "Revisar PR", Priority.HIGH);

            taskQueue.insert(low);
            taskQueue.insert(medium);
            taskQueue.insert(urgent);
            taskQueue.insert(high);

            assertEquals(urgent, taskQueue.peek());
            assertEquals(urgent, taskQueue.remove());
            assertEquals(high, taskQueue.remove());
            assertEquals(medium, taskQueue.remove());
            assertEquals(low, taskQueue.remove());
            assertTrue(taskQueue.isEmpty());
        }

        @Test
        @DisplayName("Deve preservar ordem de entrada (FIFO) em caso de empate de prioridades")
        void shouldPreserveInsertionOrderOnEqualPriorities() {
            PriorityQueue<Task> taskQueue = new PriorityQueue<>(
                    Comparator.comparingInt(task -> task.getPriority().getLevel())
            );

            Task t1 = new Task(1L, "Alta 1", Priority.HIGH);
            Task t2 = new Task(2L, "Media 1", Priority.MEDIUM);
            Task t3 = new Task(3L, "Alta 2", Priority.HIGH);
            Task t4 = new Task(4L, "Alta 3", Priority.HIGH);
            Task t5 = new Task(5L, "Urgente 1", Priority.URGENT);
            Task t6 = new Task(6L, "Alta 4", Priority.HIGH);

            taskQueue.insert(t1);
            taskQueue.insert(t2);
            taskQueue.insert(t3);
            taskQueue.insert(t4);
            taskQueue.insert(t5);
            taskQueue.insert(t6);

            assertTrue(taskQueue.isValidMaxHeap());
            assertEquals(t5, taskQueue.remove());
            assertEquals(t1, taskQueue.remove());
            assertEquals(t3, taskQueue.remove());
            assertEquals(t4, taskQueue.remove());
            assertEquals(t6, taskQueue.remove());
            assertEquals(t2, taskQueue.remove());
        }

        @Test
        @DisplayName("list deve retornar elementos ordenados por prioridade sem modificar o Heap original")
        void shouldListWithoutModifyingHeap() {
            queue.insert(20);
            queue.insert(5);
            queue.insert(40);
            queue.insert(15);

            List<Integer> listed = queue.list();
            assertEquals(List.of(40, 20, 15, 5), listed);
            assertThrows(UnsupportedOperationException.class, () -> listed.add(99));

            assertEquals(4, queue.size());
            assertEquals(40, queue.peek());
            assertTrue(queue.isValidMaxHeap());
        }
    }

    @Nested
    @DisplayName("Redimensionamento Dinâmico e Estresse")
    class ResizingAndStressTests {
        @Test
        @DisplayName("Deve redimensionar array dinamicamente ao ultrapassar capacidade inicial")
        void shouldResizeDynamicallyAndPreserveHeapInvariant() {
            int initialCapacity = queue.capacity();
            int totalElements = 200;

            List<Integer> input = new ArrayList<>();
            for (int i = 1; i <= totalElements; i++) {
                input.add(i);
            }
            Collections.shuffle(input);

            for (Integer value : input) {
                queue.insert(value);
                assertTrue(queue.isValidMaxHeap());
            }

            assertTrue(queue.capacity() > initialCapacity);
            assertEquals(totalElements, queue.size());

            for (int expected = totalElements; expected >= 1; expected--) {
                assertEquals(expected, queue.remove());
                assertTrue(queue.isValidMaxHeap());
            }

            assertTrue(queue.isEmpty());
        }

        @Test
        @DisplayName("Deve manter invariante após inserções e remoções intercaladas e clear")
        void shouldHandleInterleavedOperationsAndClear() {
            queue.insert(10);
            queue.insert(30);
            queue.insert(20);
            assertEquals(30, queue.remove());

            queue.insert(50);
            queue.insert(25);
            assertEquals(50, queue.peek());
            assertTrue(queue.isValidMaxHeap());

            queue.clear();
            assertTrue(queue.isEmpty());
            assertEquals(0, queue.size());
            assertThrows(NoSuchElementException.class, () -> queue.peek());

            queue.insert(5);
            assertEquals(5, queue.remove());
        }
    }
}
