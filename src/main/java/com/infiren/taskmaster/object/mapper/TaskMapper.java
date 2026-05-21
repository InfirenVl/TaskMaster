package com.infiren.taskmaster.object.mapper;

import com.infiren.taskmaster.object.dto.TaskDto;
import com.infiren.taskmaster.object.entity.TaskEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;


@Component
public class TaskMapper {

    public TaskEntity mapTaskDtoToTaskEntity(@NonNull TaskDto taskDtoToMap) {
         TaskEntity task = new TaskEntity();
                task.setCreatorId(taskDtoToMap.getCreatorId());
                task.setAssignedUserId(taskDtoToMap.getAssignedUserId());
                task.setTitle(taskDtoToMap.getTitle());
                task.setDescription(taskDtoToMap.getDescription());
                task.setPriority(taskDtoToMap.getPriority());
                task.setStatus(taskDtoToMap.getStatus());
                task.setCreatedDateTime(taskDtoToMap.getCreatedDateTime());
                task.setCompletedDateTime(taskDtoToMap.getCompletedDateTime());
                task.setDeadlineDate(taskDtoToMap.getDeadlineDate());

        return task;
    }

    public TaskDto mapTaskEntityToTaskDto(@NonNull TaskEntity taskEntityToMap) {
        return new TaskDto.Builder()
                .creatorId(taskEntityToMap.getCreatorId())
                .assignedUserId(taskEntityToMap.getAssignedUserId())
                .title(taskEntityToMap.getTitle())
                .description(taskEntityToMap.getDescription())
                .priority(taskEntityToMap.getPriority())
                .status(taskEntityToMap.getStatus())
                .createdDateTime(taskEntityToMap.getCreatedDateTime())
                .completedDateTime(taskEntityToMap.getCompletedDateTime())
                .deadlineDate(taskEntityToMap.getDeadlineDate())
                .build();
    }
}
