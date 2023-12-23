package ru.openblocks.management.api.dto.sprintlayout.update;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SprintLayoutUpdateRequest(
        @NotNull Long sprintId,

        @NotEmpty List<String> sprintLayout
) {
}
