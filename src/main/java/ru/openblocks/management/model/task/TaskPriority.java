package ru.openblocks.management.model.task;

import java.util.stream.Stream;

public enum TaskPriority {

    LOW(1L),

    MEDIUM(2L),

    HIGH(3L),

    CRITICAL(4L);

    private final Long priority;

    TaskPriority(Long priority) {
        this.priority = priority;
    }

    public Long asLong() {
        return priority;
    }

    public static TaskPriority of(Long priority) {
        return Stream.of(TaskPriority.values())
                .filter(taskPriority -> taskPriority.asLong().equals(priority))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

