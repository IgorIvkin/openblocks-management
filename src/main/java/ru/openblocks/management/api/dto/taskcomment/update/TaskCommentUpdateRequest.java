package ru.openblocks.management.api.dto.taskcomment.update;

import jakarta.validation.constraints.NotBlank;

public record TaskCommentUpdateRequest(
        @NotBlank String content
) {
}
