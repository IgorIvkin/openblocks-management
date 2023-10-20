package ru.openblocks.management.api.dto.user.get;

public record UserResponse(
        Long id,

        String login,

        String name,

        String shortName) {
}
