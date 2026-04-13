package com.infiren.taskmaster.controller;

import com.infiren.taskmaster.object.dto.TaskDTO;
import com.infiren.taskmaster.object.entity.TaskEntity;
import com.infiren.taskmaster.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
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
    public ResponseEntity<List<TaskDTO>> getTaskList() {
        log.info("@GET - called getTaskList()");

        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(taskService.getTaskList());
        } catch (NoSuchElementException e) {
            log.warn("@GET - getTaskList() returns empty list");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable int id) {
        log.info("@GET - called getTaskById()");

        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(taskService.getTaskById(id));
        } catch (IndexOutOfBoundsException e) {
            log.warn("@GET - getTaskById() returns null for id {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<TaskDTO> startTask(@PathVariable int id){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(taskService.startTask(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskToCreate) {
        log.info("@POST - Called method createTask");

        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(taskService.createTask(taskToCreate));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable int id,
            @RequestBody TaskDTO taskToUpdate) {

        log.info("@PUT - Called method updateTask");

        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(taskService.updateTask(id, taskToUpdate));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskDTO> updateTaskStatus(
            @PathVariable int id,
            @RequestBody TaskEntity.Status status){
        log.info("@PATCH - Called method updateTaskStatus");

        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(taskService.updateTaskStatus(id, status));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteTask(@PathVariable int id) {

        log.info("@DELETE - Called method deleteTask");

        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(taskService.deleteTask(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }
}
