package ru.openblocks.management.api.dto.taskcomment.create;

import jakarta.validation.constraints.NotBlank;

public record TaskCommentCreateRequest(
        @NotBlank String content
) {
}
