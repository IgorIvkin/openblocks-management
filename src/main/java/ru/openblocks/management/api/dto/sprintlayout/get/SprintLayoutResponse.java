package ru.openblocks.management.api.dto.sprintlayout.get;

import java.util.List;

public record SprintLayoutResponse(
        Long sprintId,

        List<String> sprintLayout
) {
}
