package ru.openblocks.management.api.dto.user.update;

import jakarta.validation.constraints.NotBlank;

public record UserUpdatePasswordRequest(
        @NotBlank String oldPassword,

        @NotBlank String password,

        @NotBlank String passwordRepeat
) {
}
