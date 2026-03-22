package com.infiren.taskmaster.service;


import com.infiren.taskmaster.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

@Service
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);
    private final HashMap<Integer, TaskEntity> tasksList = new HashMap<>();

    private final TaskEntity task1 = new TaskEntity(
            1,
            1,
            1,
            "Code App",
            "Code cool app",
            LocalDateTime.now(),
            null,
            LocalDate.now().plusDays(5L),
            TaskEntity.Priority.LOW
    );


    private final TaskEntity task2 = new TaskEntity(
            2,
            1,
            2,
            "Code App Too",
            "Code cool app too",
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(1L),
            LocalDate.now().plusDays(2L),
            TaskEntity.Priority.FIRST
    );

    private void addTasks() {

        tasksList.put(0, task1);
        tasksList.put(1, task2);
    }

    public HashMap<Integer, TaskEntity> getTaskList() {

        addTasks();
        return tasksList;
    }

    public TaskEntity getTaskById(int id) {

        addTasks();
        return tasksList.get(id);
    }


}
