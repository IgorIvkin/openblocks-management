package ru.openblocks.management.api.dto.project.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProjectCreateRequest(
        @NotBlank @Size(max = 255) String title,

        @NotBlank @Size(max = 255) String code
) {
}
