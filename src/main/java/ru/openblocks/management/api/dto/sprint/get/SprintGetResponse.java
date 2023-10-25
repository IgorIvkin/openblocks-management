package ru.openblocks.management.api.dto.sprint.get;

import java.time.LocalDate;

public record SprintGetResponse(
        String title,

        String projectCode,

        Boolean finished,

        LocalDate startDate,

        LocalDate endDate
) {
}
