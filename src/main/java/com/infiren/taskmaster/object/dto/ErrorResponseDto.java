package com.infiren.taskmaster.object.dto;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        String message,
        String details,
        LocalDateTime errorTime
) {

}
