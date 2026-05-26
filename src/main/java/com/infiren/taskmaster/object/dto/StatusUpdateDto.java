package com.infiren.taskmaster.object.dto;

import com.infiren.taskmaster.object.entity.TaskEntity;

public record StatusUpdateDto(TaskEntity.Status status) {
}
