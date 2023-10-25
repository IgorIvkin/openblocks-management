package ru.openblocks.management.api.dto.sprint.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record SprintCreateRequest(
        @NotBlank @Size(max = 255) String projectCode,

        @NotBlank @Size(max = 255) String title,

        @NotNull LocalDate startDate,

        @NotNull LocalDate endDate
) {
}
