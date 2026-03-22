package com.infiren.taskmaster.controller;

import com.infiren.taskmaster.entity.TaskEntity;
import com.infiren.taskmaster.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashMap;

@RestController
public class TaskController {

    private static final Logger log = LoggerFactory.getLogger(TaskController.class);
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {

        log.info("Constructor - TaskController(TaskService taskService)");
        this.taskService = taskService;
    }

    @GetMapping("/taskList")
    public HashMap<Integer, TaskEntity> getTaskList() {

        log.info("@GET - getTaskList()");

        if (taskService.getTaskList().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.NO_CONTENT);

        } else {

            return taskService.getTaskList();
        }
    }

    @GetMapping("/task/{id}")
    public TaskEntity getTaskById(@PathVariable int id) {

        log.info("@GET - getTaskById()");

        if (id < 0 || id >= taskService.getTaskList().size()) {

            log.warn("@GET - getTaskById() returns null for id {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id: " + id + " is out of range ");
        } else {

            return taskService.getTaskById(id);
        }

    }
}
