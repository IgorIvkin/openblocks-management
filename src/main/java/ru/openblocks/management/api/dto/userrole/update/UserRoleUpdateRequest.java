package ru.openblocks.management.api.dto.userrole.update;

import jakarta.validation.constraints.NotNull;

public record UserRoleUpdateRequest(@NotNull String title) {
}
