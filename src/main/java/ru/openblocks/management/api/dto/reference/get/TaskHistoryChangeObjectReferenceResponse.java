package ru.openblocks.management.api.dto.reference.get;

import ru.openblocks.management.model.task.TaskHistoryChangeObject;

public record TaskHistoryChangeObjectReferenceResponse(
        TaskHistoryChangeObject code,

        Long id,

        String title
) {
}
