package ru.openblocks.management.api.dto.project.get;

import java.time.Instant;

public record ProjectResponse(
        Long id,
        String title,
        String code,
        Instant createdAt
) {
}
