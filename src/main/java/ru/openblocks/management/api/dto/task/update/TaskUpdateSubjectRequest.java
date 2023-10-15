package ru.openblocks.management.api.dto.task.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskUpdateSubjectRequest(
        @NotBlank @Size(max = 255) String subject
) {
}
