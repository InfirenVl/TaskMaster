package com.infiren.taskmaster.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;
import java.time.LocalDateTime;

@JsonDeserialize(builder = TaskEntity.Builder.class)
public class TaskEntity implements Serializable {
    private final Integer id;
    private final Integer creatorId;
    private final Integer assignedUserId;
    private final String title;
    private final String description;
    public final Priority priority;
    public final Status status;
    private final LocalDateTime createdDateTime;
    private final LocalDateTime completedDateTime;
    private final LocalDateTime deadlineDate;


    private TaskEntity(Builder builder) {
        this.id = builder.id;
        this.creatorId = builder.creatorId;
        this.assignedUserId = builder.assignedUserId;
        this.title = builder.title;
        this.description = builder.description;
        this.createdDateTime = builder.createdDateTime;
        this.completedDateTime = builder.completedDateTime;
        this.deadlineDate = builder.deadlineDate;
        this.priority = builder.priority;
        this.status = builder.status;
    }

    public static class Builder{
        @JsonProperty("id")
        private Integer id;

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
        private Priority priority;

        @JsonProperty("status")
        private Status status;

        public Builder id(final Integer id) {
            if (id == null || id<0){
                throw new IllegalArgumentException("Id can't be not null or less 0");
            }
            this.id = id;
            return this;
        }

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

        public Builder priority(final Priority priority){
            this.priority = priority;
            return this;
        }

        public Builder status(final Status status){
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

        public TaskEntity build(){
            return new TaskEntity(this);
        }

    }

    public enum Priority {
        FIRST,
        HIGH,
        MEDIUM,
        LOW
    }

    public enum Status {
        CREATED,
        IN_PROGRESS,
        COMPLETED
    }

    public String getDescription() {
        return description;
    }

    public Integer getId() {
        return id;
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

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public LocalDateTime getCompletedDateTime() {
        return completedDateTime;
    }

    public LocalDateTime getDeadlineDate() {
        return deadlineDate;
    }



}

