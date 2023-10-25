package ru.openblocks.management.api.dto.task.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskUpdateExplanationRequest(
        @NotBlank @Size(max = 10000) String explanation
) {
}
