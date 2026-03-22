package com.infiren.taskmaster.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class TaskEntity {
    private final int id;
    private final int creatorId;
    private final int assignedUserId;
    private final String title;
    private final String description;
    private final LocalDateTime createdDateTime;
    private final LocalDateTime completedDateTime;
    private final LocalDate deadlineDate;
    public final Priority priority;
    public final Status status;

    public TaskEntity(int id, int creatorId, int assignedUserId, String title, String description, LocalDateTime createdDateTime, LocalDateTime completedDateTime, LocalDate deadlineDate, Priority priority) {
        this.id = id;
        this.creatorId = creatorId;
        this.assignedUserId = assignedUserId;
        this.title = title;
        this.description = description;
        this.createdDateTime = createdDateTime;
        this.completedDateTime = completedDateTime;
        this.deadlineDate = deadlineDate;
        this.priority = priority;
        this.status = Status.CREATED;
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

    public int getId() {
        return id;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public int getAssignedUserId() {
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

    public LocalDate getDeadlineDate() {
        return deadlineDate;
    }



}
