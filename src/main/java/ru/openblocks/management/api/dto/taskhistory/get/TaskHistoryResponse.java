package ru.openblocks.management.api.dto.taskhistory.get;

import ru.openblocks.management.model.task.TaskHistoryChangeObject;

import java.time.Instant;

public record TaskHistoryResponse(
        Instant createdAt,

        TaskHistoryChangeObject changeObject,

        String previousValue,

        String newValue,

        TaskHistoryUserResponse author
) {
}
