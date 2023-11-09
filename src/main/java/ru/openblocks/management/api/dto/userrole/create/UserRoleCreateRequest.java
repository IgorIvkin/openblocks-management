package ru.openblocks.management.api.dto.userrole.create;

import jakarta.validation.constraints.NotNull;

public record UserRoleCreateRequest(
        @NotNull String title,

        @NotNull String code
) {
}
