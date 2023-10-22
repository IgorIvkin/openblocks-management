package ru.openblocks.management.api.dto.taskfile.get;

import java.time.Instant;

public record TaskFileGetResponse(
        Long id,

        Instant createdAt,

        String fileName,

        String filePath,

        String mimeType
) {
}
