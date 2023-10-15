package ru.openblocks.management.model.task;

import java.util.stream.Stream;

public enum TaskType {

    TASK(1L),

    BUG(2L),

    STORY(3L);

    private final Long type;

    TaskType(Long type) {
        this.type = type;
    }

    public Long asLong() {
        return type;
    }

    public static TaskType of(Long type) {
        return Stream.of(TaskType.values())
                .filter(taskType -> taskType.asLong().equals(type))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
