package com.ads.quest_tracker.service;

import com.ads.quest_tracker.model.SubTask;
import com.ads.quest_tracker.model.Task;
import com.ads.quest_tracker.structures.linkedlist.LinkedList;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
public class SubtaskService {
    public SubTask addSubtask(Task task, SubTask subtask) {
        Objects.requireNonNull(task, "A tarefa principal não pode ser nula.");
        Objects.requireNonNull(subtask, "A subtarefa não pode ser nula.");

        LinkedList<SubTask> subtasks = task.getSubtasks();
        if (findSubtaskById(subtasks, subtask.getId()).isPresent()) {
            throw new IllegalArgumentException(
                    "Já existe uma subtarefa com o identificador " + subtask.getId() + " nesta tarefa."
            );
        }

        subtasks.add(subtask);
        return subtask;
    }

    public List<SubTask> listSubtasks(Task task) {
        Objects.requireNonNull(task, "A tarefa principal não pode ser nula.");

        LinkedList<SubTask> subtasks = task.getSubtasks();
        List<SubTask> result = new ArrayList<>(subtasks.size());
        for (SubTask subTask : subtasks) {
            result.add(subTask);
        }
        return Collections.unmodifiableList(result);
    }

    public SubTask markSubtaskAsCompleted(Task task, Long subtaskId) {
        Objects.requireNonNull(task, "A tarefa principal não pode ser nula.");
        Objects.requireNonNull(subtaskId, "O identificador da subtarefa não pode ser nulo.");
        if (subtaskId <= 0) {
            throw new IllegalArgumentException("O identificador da subtarefa deve ser positivo.");
        }

        SubTask target = findSubtaskById(task.getSubtasks(), subtaskId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Subtarefa com identificador " + subtaskId + " não encontrada na tarefa " + task.getId() + "."
                ));

        target.markAsCompleted();
        return target;
    }

    public List<SubTask> listPendingSubtasks(Task task) {
        Objects.requireNonNull(task, "A tarefa principal não pode ser nula.");

        List<SubTask> pending = new ArrayList<>();
        for (SubTask subTask : task.getSubtasks()) {
            if (!subTask.isCompleted()) {
                pending.add(subTask);
            }
        }
        return Collections.unmodifiableList(pending);
    }

    private Optional<SubTask> findSubtaskById(LinkedList<SubTask> subtasks, Long subtaskId) {
        for (SubTask current : subtasks) {
            if (Objects.equals(current.getId(), subtaskId)) {
                return Optional.of(current);
            }
        }
        return Optional.empty();
    }
}
