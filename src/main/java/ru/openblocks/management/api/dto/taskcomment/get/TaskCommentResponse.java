package ru.openblocks.management.api.dto.taskcomment.get;

import java.time.Instant;

public record TaskCommentResponse(
        Long id,

        String content,

        Instant createdAt,

        TaskCommentAuthorResponse author
) {
}
