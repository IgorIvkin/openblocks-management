package ru.openblocks.management.api.dto.tasklink.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.openblocks.management.model.task.TaskLinkType;

public record TaskLinkCreateRequest(
        @NotBlank String taskCode,

        @NotBlank String connectedTaskCode,

        @NotNull TaskLinkType linkType
) {
}
