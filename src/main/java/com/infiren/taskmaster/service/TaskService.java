package com.infiren.taskmaster.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.infiren.taskmaster.object.dto.TaskDTO;
import com.infiren.taskmaster.object.entity.TaskEntity;
import com.infiren.taskmaster.object.mapper.ObjectMapper;
import com.infiren.taskmaster.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository taskRepository;
    private final ObjectMapper objectMapper;


    public TaskService(TaskRepository taskRepository, ObjectMapper objectMapper) {
        this.taskRepository = taskRepository;
        this.objectMapper = objectMapper;
    }

    public List<TaskDTO> getTaskList() {
        List<TaskEntity> allTasks = taskRepository.findAll();

        List<TaskDTO> tasksList = allTasks.stream()
                .map(taskEntity ->
                        new TaskDTO.Builder()
                                .creatorId(taskEntity.getCreatorId())
                                .assignedUserId(taskEntity.getAssignedUserId())
                                .title(taskEntity.getTitle())
                                .description(taskEntity.getDescription())
                                .priority(taskEntity.getPriority())
                                .status(taskEntity.getStatus())
                                .createdDateTime(taskEntity.getCreatedDateTime())
                                .completedDateTime(taskEntity.getCompletedDateTime())
                                .deadlineDate(taskEntity.getDeadlineDate())
                                .build()
                ).toList();

        if (tasksList.isEmpty()) {
            throw new NoSuchElementException("Tasks is empty");
        }

        return tasksList;
    }

    public TaskDTO getTaskById(int id) {

        TaskEntity existingTask = getExistingTask(id);

        return objectMapper.mapTaskEntityToTaskDto(existingTask);
    }

    public TaskDTO createTask(TaskDTO taskToCreate) {
        TaskEntity task = taskRepository.save(
                objectMapper.mapTaskDtoToTaskEntity(taskToCreate)
        );
        return objectMapper.mapTaskEntityToTaskDto(task);
    }

    public TaskDTO updateTask(int id, @NonNull TaskDTO taskToUpdate) {

        TaskEntity existingTask = getExistingTask(id);

        if(taskToUpdate.getStatus().equals(TaskEntity.Status.COMPLETED) &&
                existingTask.getStatus().equals(TaskEntity.Status.COMPLETED)){
            throw new IllegalStateException("Task already completed.");
        }


        TaskEntity updatedTask = new TaskEntity.Builder()
                .id(existingTask.getId())
                .creatorId(existingTask.getCreatorId())
                .assignedUserId(taskToUpdate.getAssignedUserId())
                .title(taskToUpdate.getTitle())
                .description(taskToUpdate.getDescription())
                .priority(taskToUpdate.getPriority())
                .status(taskToUpdate.getStatus())
                .createdDateTime(existingTask.getCreatedDateTime())
                .completedDateTime(taskToUpdate.getCompletedDateTime())
                .deadlineDate(taskToUpdate.getDeadlineDate())
                .build();

        taskRepository.save(updatedTask);

        return objectMapper.mapTaskEntityToTaskDto(updatedTask);
    }

    public TaskDTO updateTaskStatus(int id, TaskEntity.Status newStatus){


        TaskEntity existingTask = getExistingTask(id);


        if(newStatus.equals(TaskEntity.Status.COMPLETED) &&
                existingTask.getStatus().equals(TaskEntity.Status.COMPLETED)){
            throw new IllegalStateException("Task already completed.");
        }


        TaskEntity updatedTask = new TaskEntity.Builder()
                .id(existingTask.getId())
                .creatorId(existingTask.getCreatorId())
                .assignedUserId(existingTask.getAssignedUserId())
                .title(existingTask.getTitle())
                .description(existingTask.getDescription())
                .priority(existingTask.getPriority())
                .status(newStatus)
                .createdDateTime(existingTask.getCreatedDateTime())
                .completedDateTime(existingTask.getCompletedDateTime())
                .deadlineDate(existingTask.getDeadlineDate())
                .build();


        taskRepository.save(updatedTask);

        return objectMapper.mapTaskEntityToTaskDto(updatedTask);
    }

    public TaskDTO startTask(int id) {

        TaskEntity existingTask = getExistingTask(id);

        if(existingTask.getAssignedUserId() == null){
            throw new IllegalArgumentException("No Assigned User for Task!");
        }

        List<TaskEntity> taskList = taskRepository.findByAssignedUserIdAndStatus(existingTask.getAssignedUserId(), TaskEntity.Status.IN_PROGRESS);

        if (taskList.size()>=4){
            throw new IndexOutOfBoundsException("No more active task");
        }

        TaskEntity startedTask = new TaskEntity.Builder()
                .id(existingTask.getId())
                .creatorId(existingTask.getCreatorId())
                .assignedUserId(existingTask.getAssignedUserId())
                .title(existingTask.getTitle())
                .description(existingTask.getDescription())
                .priority(existingTask.getPriority())
                .status(TaskEntity.Status.IN_PROGRESS)
                .createdDateTime(existingTask.getCreatedDateTime())
                .completedDateTime(existingTask.getCompletedDateTime())
                .deadlineDate(existingTask.getDeadlineDate())
                .build();

        taskRepository.save(startedTask);

        return objectMapper.mapTaskEntityToTaskDto(startedTask);
    }

    public Boolean deleteTask(int id) {

        TaskEntity existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: " + id));

       taskRepository.delete(existingTask);

       return taskRepository.findById(id).isEmpty();
    }

    private TaskEntity getExistingTask(int id){

        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: " + id));
    }


}
