package com.infiren.taskmaster.object.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.infiren.taskmaster.object.entity.TaskEntity;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;
import java.time.LocalDateTime;

@JsonDeserialize(builder = TaskDTO.Builder.class)
public class TaskDTO implements Serializable {
    private final Integer creatorId;
    private final Integer assignedUserId;
    private final String title;
    private final String description;
    private final TaskEntity.Priority priority;
    private final TaskEntity.Status status;
    private final LocalDateTime createdDateTime;
    private final LocalDateTime completedDateTime;
    private final LocalDateTime deadlineDate;

    public static class Builder{
        @JsonProperty("creatorId")
        private Integer creatorId;

        @JsonProperty("assignedUserId")
        private Integer assignedUserId;

        @JsonProperty("title")
        private String title;

        @JsonProperty("description")
        private String description;

        @JsonProperty("createdDateTime")
        private LocalDateTime createdDateTime;

        @JsonProperty("completedDateTime")
        private LocalDateTime completedDateTime;

        @JsonProperty("deadlineDate")
        private LocalDateTime deadlineDate;

        @JsonProperty("priority")
        private TaskEntity.Priority priority;

        @JsonProperty("status")
        private TaskEntity.Status status;

        public Builder creatorId(final Integer creatorId){
            if (creatorId == null || creatorId<0){
                throw new IllegalArgumentException("Id can't be null or less 0");
            }
            this.creatorId = creatorId;
            return this;
        }

        public Builder assignedUserId(final Integer assignedUserId){
            if (assignedUserId == null || assignedUserId<0){
                throw new IllegalArgumentException("Id can't be null or less 0");
            }
            this.assignedUserId = assignedUserId;
            return this;
        }

        public Builder title(final String title){
            if(title == null){
                throw new IllegalArgumentException("Tittle can't be null");
            }
            this.title = title;
            return this;
        }

        public Builder description(final String description){
            this.description = description;
            return this;
        }

        public Builder deadlineDate(final LocalDateTime deadlineDate){
            this.deadlineDate = deadlineDate;
            return this;
        }

        public Builder priority(final TaskEntity.Priority priority){
            this.priority = priority;
            return this;
        }

        public Builder status(final TaskEntity.Status status){
            this.status = status;
            return this;
        }

        public Builder createdDateTime(final LocalDateTime createdDateTime){
            this.createdDateTime = createdDateTime;
            return this;
        }

        public Builder completedDateTime(final LocalDateTime completedDateTime){
            this.completedDateTime = completedDateTime;
            return this;
        }

        public TaskDTO build(){
            return new TaskDTO(this);
        }

    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public Integer getAssignedUserId() {
        return assignedUserId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskEntity.Priority getPriority() {
        return priority;
    }

    public LocalDateTime getDeadlineDate() {
        return deadlineDate;
    }

    public LocalDateTime getCompletedDateTime() {
        return completedDateTime;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public TaskEntity.Status getStatus() {
        return status;
    }

    private TaskDTO(Builder builder) {
        this.creatorId = builder.creatorId;
        this.assignedUserId = builder.assignedUserId;
        this.title = builder.title;
        this.description = builder.description;
        this.priority = builder.priority;
        this.status = builder.status;
        this.createdDateTime = builder.createdDateTime;
        this.completedDateTime = builder.completedDateTime;
        this.deadlineDate = builder.deadlineDate;
    }
}
