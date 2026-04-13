package com.infiren.taskmaster.object.mapper;

import com.infiren.taskmaster.object.dto.TaskDTO;
import com.infiren.taskmaster.object.entity.TaskEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;


@Component
public class ObjectMapper {

    public TaskEntity mapTaskDtoToTaskEntity(@NonNull TaskDTO taskDtoToMap) {
        return new TaskEntity.Builder()
                .creatorId(taskDtoToMap.getCreatorId())
                .assignedUserId(taskDtoToMap.getAssignedUserId())
                .title(taskDtoToMap.getTitle())
                .description(taskDtoToMap.getDescription())
                .priority(taskDtoToMap.getPriority())
                .status(taskDtoToMap.getStatus())
                .createdDateTime(taskDtoToMap.getCreatedDateTime())
                .completedDateTime(taskDtoToMap.getCompletedDateTime())
                .deadlineDate(taskDtoToMap.getDeadlineDate())
                .build();
    }

    public TaskDTO mapTaskEntityToTaskDto(@NonNull TaskEntity taskEntityToMap) {
        return new TaskDTO.Builder()
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
