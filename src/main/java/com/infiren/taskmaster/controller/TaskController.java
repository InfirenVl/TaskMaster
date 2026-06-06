package com.infiren.taskmaster.controller;

import com.infiren.taskmaster.object.dto.StatusUpdateDto;
import com.infiren.taskmaster.object.dto.TaskDto;
import com.infiren.taskmaster.object.entity.TaskEntity;
import com.infiren.taskmaster.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<TaskDto>> getTaskList() {
        log.info("@GET - called getTaskList()");

        return ResponseEntity.status(HttpStatus.OK)
                .body(taskService.getTaskList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable int id) {
        log.info("@GET - called getTaskById()");

        return ResponseEntity.status(HttpStatus.OK)
                .body(taskService.getTaskById(id));
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<TaskDto> startTask(@PathVariable int id) {
        log.info("@POST - Called method startTask");

        return ResponseEntity.status(HttpStatus.OK).body(taskService.startTask(id));
    }

    @PostMapping("")
    public ResponseEntity<TaskDto> createTask(@RequestBody @Valid TaskDto taskToCreate) {
        log.info("@POST - Called method createTask");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.createTask(taskToCreate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(
            @PathVariable int id,
            @RequestBody @Valid TaskDto taskToUpdate) {

        log.info("@PUT - Called method updateTask");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.updateTask(id, taskToUpdate));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskDto> updateTaskStatus(
            @PathVariable int id,
            @RequestBody @Valid StatusUpdateDto dto) {
        log.info("@PATCH - Called method updateTaskStatus");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.updateTaskStatus(id, dto.status()));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteTask(@PathVariable int id) {
        log.info("@DELETE - Called method deleteTask");

        return ResponseEntity.status(HttpStatus.OK)
                .body(taskService.deleteTask(id));
    }
}
