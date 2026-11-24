package com.ads.quest_tracker.service;

import com.ads.quest_tracker.model.Task;
import com.ads.quest_tracker.model.TaskStatus;
import com.ads.quest_tracker.structures.priorityqueue.PriorityQueue;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class ExecutionQueueService {
    private final PriorityQueue<Task> executionQueue;

    public ExecutionQueueService() {
        this.executionQueue = new PriorityQueue<>(
                Comparator.comparingInt(task -> task.getPriority().getLevel())
        );
    }

    public void enqueueTask(Task task) {
        Objects.requireNonNull(task, "A tarefa a ser enfileirada não pode ser nula.");
        if (task.getStatus() == TaskStatus.COMPLETED) {
            throw new IllegalArgumentException(
                    "Não é possível adicionar à fila de execução uma tarefa já concluída."
            );
        }
        executionQueue.insert(task);
    }

    public Task peekNextTask() {
        return executionQueue.peek();
    }

    public Task executeNextTask() {
        Task nextTask = executionQueue.remove();
        nextTask.setStatus(TaskStatus.COMPLETED);
        return nextTask;
    }

    public List<Task> listQueuedTasks() {
        return executionQueue.list();
    }

    public boolean hasPendingTasks() {
        return !executionQueue.isEmpty();
    }

    public int queueSize() {
        return executionQueue.size();
    }
}
