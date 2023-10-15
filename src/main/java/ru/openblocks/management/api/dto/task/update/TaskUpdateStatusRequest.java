package ru.openblocks.management.api.dto.task.update;

import jakarta.validation.constraints.NotNull;
import ru.openblocks.management.model.task.TaskStatus;

public record TaskUpdateStatusRequest(
        @NotNull TaskStatus status) {
}
