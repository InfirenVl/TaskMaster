package com.infiren.taskmaster.service;

import com.infiren.taskmaster.object.dto.TaskDto;
import com.infiren.taskmaster.object.entity.TaskEntity;
import com.infiren.taskmaster.object.mapper.TaskMapper;
import com.infiren.taskmaster.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;


    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public List<TaskDto> getTaskList() {
        List<TaskEntity> allTasks = taskRepository.findAll();


        List<TaskDto> tasksList = allTasks.stream()
                .map(taskMapper::mapTaskEntityToTaskDto
                ).toList();

        if (tasksList.isEmpty()) {
            throw new EntityNotFoundException("Tasks List is empty");
        }

        return tasksList;
    }

    public TaskDto getTaskById(int id) {
        TaskEntity existingTask = getExistingTask(id);
        return taskMapper.mapTaskEntityToTaskDto(existingTask);
    }

    public TaskDto createTask(TaskDto taskToCreate) {
        TaskDto task = new TaskDto.Builder()
                .creatorId(taskToCreate.getCreatorId())
                .assignedUserId(taskToCreate.getAssignedUserId())
                .title(taskToCreate.getTitle())
                .description(taskToCreate.getDescription())
                .priority(taskToCreate.getPriority())
                .status(taskToCreate.getStatus())
                .createdDateTime(LocalDateTime.now())
                .completedDateTime(null)
                .deadlineDate(taskToCreate.getDeadlineDate())
                .build();

        taskRepository.save(taskMapper.mapTaskDtoToTaskEntity(task));
        return task;
    }

    public TaskDto updateTask(int id, @NonNull TaskDto taskToUpdate) {

        TaskEntity existingTask = getExistingTask(id);

        if (taskToUpdate.getStatus().equals(TaskEntity.Status.COMPLETED) &&
                existingTask.getStatus().equals(TaskEntity.Status.COMPLETED)) {
            throw new IllegalStateException("Task already completed.");
        }


        TaskEntity updatedTask = new TaskEntity();
                updatedTask.setId(existingTask.getId());
                updatedTask.setCreatorId(existingTask.getCreatorId());
                updatedTask.setAssignedUserId(taskToUpdate.getAssignedUserId());
                updatedTask.setTitle(taskToUpdate.getTitle());
                updatedTask.setDescription(taskToUpdate.getDescription());
                updatedTask.setPriority(taskToUpdate.getPriority());
                updatedTask.setStatus(taskToUpdate.getStatus());
                updatedTask.setCreatedDateTime(existingTask.getCreatedDateTime());
                updatedTask.setCompletedDateTime(taskToUpdate.getCompletedDateTime());
                updatedTask.setDeadlineDate(taskToUpdate.getDeadlineDate());

        taskRepository.save(updatedTask);

        return taskMapper.mapTaskEntityToTaskDto(updatedTask);
    }

    public TaskDto updateTaskStatus(int id, TaskEntity.Status newStatus) {


        TaskEntity existingTask = getExistingTask(id);


        if (newStatus.equals(TaskEntity.Status.COMPLETED) &&
                existingTask.getStatus().equals(TaskEntity.Status.COMPLETED)) {
            throw new IllegalStateException("Task already completed.");
        }


        TaskEntity updatedTask = new TaskEntity();
                updatedTask.setId(existingTask.getId());
                updatedTask.setCreatorId(existingTask.getCreatorId());
                updatedTask.setAssignedUserId(existingTask.getAssignedUserId());
                updatedTask.setTitle(existingTask.getTitle());
                updatedTask.setDescription(existingTask.getDescription());
                updatedTask.setPriority(existingTask.getPriority());
                updatedTask.setStatus(newStatus);
                updatedTask.setCreatedDateTime(existingTask.getCreatedDateTime());
                updatedTask.setCompletedDateTime(existingTask.getCompletedDateTime());
                updatedTask.setDeadlineDate(existingTask.getDeadlineDate());



        taskRepository.save(updatedTask);

        return taskMapper.mapTaskEntityToTaskDto(updatedTask);
    }

    public TaskDto startTask(int id) {

        TaskEntity existingTask = getExistingTask(id);

        if (existingTask.getAssignedUserId() == null) {
            throw new IllegalArgumentException("No Assigned User for Task!");
        }

        List<TaskEntity> taskList = taskRepository.findByAssignedUserIdAndStatus(existingTask.getAssignedUserId(), TaskEntity.Status.IN_PROGRESS);

        if (taskList.size() >= 4) {
            throw new IllegalArgumentException("No more active task for this user " + existingTask.getAssignedUserId());
        }

        TaskEntity startedTask = new TaskEntity();
                startedTask.setId(existingTask.getId());
                startedTask.setCreatorId(existingTask.getCreatorId());
                startedTask.setAssignedUserId(existingTask.getAssignedUserId());
                startedTask.setTitle(existingTask.getTitle());
                startedTask.setDescription(existingTask.getDescription());
                startedTask.setPriority(existingTask.getPriority());
                startedTask.setStatus(TaskEntity.Status.IN_PROGRESS);
                startedTask.setCreatedDateTime(existingTask.getCreatedDateTime());
                startedTask.setCompletedDateTime(existingTask.getCompletedDateTime());
                startedTask.setDeadlineDate(existingTask.getDeadlineDate());

        taskRepository.save(startedTask);

        return taskMapper.mapTaskEntityToTaskDto(startedTask);
    }

    public Boolean deleteTask(int id) {

        TaskEntity existingTask = getExistingTask(id);

        taskRepository.delete(existingTask);

        return taskRepository.findById(id).isEmpty();
    }

    private TaskEntity getExistingTask(int id) {

        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: " + id));
    }


}
