package ru.openblocks.management.api.dto.tasklink.get;

import ru.openblocks.management.model.task.TaskLinkType;

public record TaskLinkResponse(
        Long id,

        TaskLinkTaskResponse connectedTask,

        TaskLinkType linkType
) {
}
