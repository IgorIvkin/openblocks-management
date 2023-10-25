package ru.openblocks.management.api.dto.reference.get;

import ru.openblocks.management.model.task.TaskLinkType;

public record TaskLinkTypeReferenceResponse(
        TaskLinkType code,

        Long id,

        String title
) {
}
