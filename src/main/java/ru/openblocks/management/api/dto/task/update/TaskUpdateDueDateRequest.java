package ru.openblocks.management.api.dto.task.update;

import java.time.LocalDate;

public record TaskUpdateDueDateRequest(
        LocalDate dueDate
) {
}
