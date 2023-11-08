package ru.openblocks.management.api.dto.user.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateNameRequest(
        @NotBlank @Size(max = 255) String name
) {
}