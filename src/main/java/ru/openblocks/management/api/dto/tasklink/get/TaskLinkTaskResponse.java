package ru.openblocks.management.api.dto.tasklink.get;

import ru.openblocks.management.model.task.TaskStatus;

public record TaskLinkTaskResponse(
        String code,

        String subject,

        TaskStatus status
) {
}
