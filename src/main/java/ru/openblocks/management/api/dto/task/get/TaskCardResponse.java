package ru.openblocks.management.api.dto.task.get;

import ru.openblocks.management.model.task.TaskPriority;
import ru.openblocks.management.model.task.TaskStatus;
import ru.openblocks.management.model.task.TaskType;

import java.time.Instant;
import java.time.LocalDate;

public record TaskCardResponse(
        String code,

        TaskCardProjectResponse project,

        TaskStatus status,

        TaskType taskType,

        TaskPriority priority,

        String subject,

        String explanation,

        Instant createdAt,

        Instant updatedAt,

        TaskCardUserResponse owner,

        TaskCardUserResponse executor,

        LocalDate dueDate,

        Integer estimation
) {
}
