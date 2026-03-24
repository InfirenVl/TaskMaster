package com.infiren.taskmaster.controller;

import com.infiren.taskmaster.entity.TaskEntity;
import com.infiren.taskmaster.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final Logger log = LoggerFactory.getLogger(TaskController.class);
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        log.info("Constructor - TaskController(TaskService taskService)");
        this.taskService = taskService;
    }

    @GetMapping("")
    public ResponseEntity<HashMap<Integer, TaskEntity>> getTaskList() {
        log.info("@GET - getTaskList()");

        try{
            return ResponseEntity.status(HttpStatus.OK).body(taskService.getTaskList());
        } catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }


    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskEntity> getTaskById(@PathVariable int id) {
        log.info("@GET - getTaskById()");
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(taskService.getTaskById(id));
        } catch (NoSuchElementException e) {
            log.warn("@GET - getTaskById() returns null for id {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("")
    public ResponseEntity<TaskEntity> createTask(@RequestBody TaskEntity taskToCreate) {
        log.info("@POST - Called method createTask");

        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(taskService.createTask(taskToCreate));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskEntity> updateTask(
            @PathVariable int id,
            @RequestBody TaskEntity taskToUpdate) {

        log.info("@PUT - Called method updateTask");

        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(taskService.updateTask(id, taskToUpdate));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteTask(@PathVariable int id) {

        log.info("@DELETE - Called method deleteTask");

        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(taskService.deleteTask(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }
}
