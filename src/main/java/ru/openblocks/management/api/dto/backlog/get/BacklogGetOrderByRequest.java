package ru.openblocks.management.api.dto.backlog.get;

import ru.openblocks.management.model.backlog.BacklogOrderByType;

public record BacklogGetOrderByRequest(
        BacklogOrderByType type
) {
}
