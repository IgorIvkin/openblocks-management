package ru.openblocks.management.model.backlog;

import java.util.stream.Stream;

public enum BacklogOrderByType {

    PRIORITY_DESC(1L, "Приоритет (сначала высокий)"),

    PRIORITY_ASC(2L, "Приоритет (сначала низкий)");

    private final Long id;

    private final String title;

    BacklogOrderByType(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long asLong() {
        return id;
    }

    public String asText() {
        return title;
    }

    public static BacklogOrderByType of(Long orderById) {
        return Stream.of(BacklogOrderByType.values())
                .filter(orderBy -> orderBy.asLong().equals(orderById))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
