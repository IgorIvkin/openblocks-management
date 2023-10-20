package ru.openblocks.management.model.task;

import java.util.stream.Stream;

public enum TaskType {

    TASK(1L, "Задача"),

    BUG(2L, "Дефект"),

    STORY(3L, "История");

    private final Long type;

    private final String title;

    TaskType(Long type, String title) {
        this.type = type;
        this.title = title;
    }

    public Long asLong() {
        return type;
    }

    public String asText() {
        return this.title;
    }

    public static TaskType of(Long type) {
        return Stream.of(TaskType.values())
                .filter(taskType -> taskType.asLong().equals(type))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
