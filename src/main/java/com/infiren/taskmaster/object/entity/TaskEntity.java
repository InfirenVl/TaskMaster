package com.infiren.taskmaster.object.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;


@Entity
@Table(name = "tasks")
public class TaskEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Integer id;

    @NotNull
    @Column(name = "creator_id")
    private final Integer creatorId;

    @Nullable
    @Column(name = "assigned_user_id")
    private final Integer assignedUserId;

    @NotNull
    @Column(name = "title")
    private final String title;

    @Nullable
    @Column(name = "description")
    private final String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private final Priority priority;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private final Status status;

    @NotNull
    @FutureOrPresent
    @Column(name = "created_date_time")
    private final LocalDateTime createdDateTime;

    @Nullable
    @Future
    @Column(name = "completed_date_time")
    private final LocalDateTime completedDateTime;

    @Nullable
    @Column(name = "deadline_date")
    private final LocalDateTime deadlineDate;

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
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

    public Priority getPriority(){
        return priority;
    }

    public Status getStatus(){
        return status;
    }

    public static class Builder{
        private Integer id;
        private Integer creatorId;

        @JsonProperty("assignedUserId")
        private Integer assignedUserId;

        private String title;
        private String description;
        private LocalDateTime createdDateTime;
        private LocalDateTime completedDateTime;
        private LocalDateTime deadlineDate;
        private Priority priority;
        private Status status;

        public Builder id(final Integer id){
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
        COMPLETED;

        @JsonCreator
        public static Status fromObject(Map<String, Object> props) {
            return Status.valueOf((String) props.get("status"));
        }

        @JsonCreator
        public static Status fromString(String value) {
            return Status.valueOf(value.toUpperCase());
        }
    }

    protected TaskEntity() {
        this.id = null;
        this.creatorId = null;
        this.assignedUserId = null;
        this.title = null;
        this.description = null;
        this.priority = null;
        this.status = null;
        this.createdDateTime = null;
        this.completedDateTime = null;
        this.deadlineDate = null;
    }

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

    @Override
    public String toString() {
        return "TaskEntity{" +
                "id=" + id +
                ", creatorId=" + creatorId +
                ", assignedUserId=" + assignedUserId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", status=" + status +
                ", createdDateTime=" + createdDateTime +
                ", completedDateTime=" + completedDateTime +
                ", deadlineDate=" + deadlineDate +
                '}';
    }
}

