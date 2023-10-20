package ru.openblocks.management.api.dto.reference.get;

import ru.openblocks.management.model.task.TaskPriority;

public record TaskPriorityReferenceResponse(
        TaskPriority code,

        Long id,

        String title,

        String symbol
) {
}
