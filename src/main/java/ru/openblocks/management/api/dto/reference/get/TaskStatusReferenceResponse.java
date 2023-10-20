package ru.openblocks.management.api.dto.reference.get;

import ru.openblocks.management.model.task.TaskStatus;

public record TaskStatusReferenceResponse(
        TaskStatus code,

        Long id,

        String title
) {
}
