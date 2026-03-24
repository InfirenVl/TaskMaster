package com.infiren.taskmaster.service;

import com.infiren.taskmaster.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);
    private final HashMap<Integer, TaskEntity> tasksList;
    private final AtomicInteger idCounter;

    public TaskService() {
        tasksList = new HashMap<Integer, TaskEntity>();
        idCounter = new AtomicInteger();
    }

    public HashMap<Integer, TaskEntity> getTaskList() {
        if (tasksList.isEmpty()) {
            throw new NoSuchElementException("HashMap is empty");
        }
        return tasksList;
    }

    public TaskEntity getTaskById(int id) {
        if (!tasksList.containsKey(id)) {
            throw new NoSuchElementException("Can't find id=" + id);
        }
        return tasksList.get(id);
    }


    public TaskEntity createTask(TaskEntity taskToCreate) {
        if (taskToCreate.getId() != null) {
            throw new IllegalArgumentException("An ID can't be set before creation");
        }

        TaskEntity task = new TaskEntity.Builder()
                .id(idCounter.getAndIncrement())
                .creatorId(taskToCreate.getCreatorId())
                .assignedUserId(taskToCreate.getAssignedUserId())
                .title(taskToCreate.getTitle())
                .description(taskToCreate.getDescription())
                .deadlineDate(taskToCreate.getDeadlineDate())
                .priority(taskToCreate.priority)
                .status(TaskEntity.Status.CREATED)
                .createdDateTime(LocalDateTime.now())
                .completedDateTime(null)
                .build();

        tasksList.put(task.getId(), task);
        return task;
    }

    public TaskEntity updateTask(int id, TaskEntity taskToUpdate) {
        if (!tasksList.containsKey(id)) {
            throw new NoSuchElementException("Can't find id=" + id);
        }
        if (!(tasksList.get(id).status==TaskEntity.Status.COMPLETED) ||
                taskToUpdate.status!= TaskEntity.Status.COMPLETED){
            TaskEntity task = new TaskEntity.Builder()
                    .id(id)
                    .creatorId(taskToUpdate.getCreatorId())
                    .assignedUserId(taskToUpdate.getAssignedUserId())
                    .title(taskToUpdate.getTitle())
                    .description(taskToUpdate.getDescription())
                    .deadlineDate(taskToUpdate.getDeadlineDate())
                    .priority(taskToUpdate.priority)
                    .status(taskToUpdate.status)
                    .createdDateTime(tasksList.get(id).getCreatedDateTime())
                    .completedDateTime(taskToUpdate.getCompletedDateTime())
                    .build();
            tasksList.put(id, task);
            return task;
        }else{
            //Replace to custom exception
            throw new IllegalStateException("Task already completed");
        }
    }

    public Boolean deleteTask(int id) {
        if (tasksList.containsKey(id)) {
            tasksList.remove(id);
            return true;
        } else {
            throw new NoSuchElementException();
        }

    }
}
