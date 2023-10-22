package ru.openblocks.management.model.task;

import java.util.stream.Stream;

public enum TaskLinkType {

    ASSOCIATED(1L, "Связано с"),

    PARENT_OF(2L, "Родитель для"),

    CHILD_OF(3L, "Потомок для");

    private final Long linkType;

    private final String title;

    TaskLinkType(Long linkType, String title) {
        this.linkType = linkType;
        this.title = title;
    }

    public Long asLong() {
        return linkType;
    }

    public String asText() {
        return title;
    }

    public static TaskLinkType of(Long linkType) {
        return Stream.of(TaskLinkType.values())
                .filter(type -> type.asLong().equals(linkType))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
