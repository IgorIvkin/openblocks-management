package ru.openblocks.management.api.dto.projectaccess.get;

public record ProjectAccessResponse(
        Long userId,

        Boolean projectAdmin,

        ProjectAccessUserResponse user
) {
}
