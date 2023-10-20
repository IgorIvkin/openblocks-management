package ru.openblocks.management.api.dto.reference.get;

import ru.openblocks.management.model.task.TaskType;

public record TaskTypeReferenceResponse(
        TaskType code,

        Long id,

        String title
) {
}
