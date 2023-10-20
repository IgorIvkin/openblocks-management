package ru.openblocks.management.model.task;

import java.util.stream.Stream;

public enum TaskPriority {

    LOW(1L, "Низкий", "&#129047;"),

    MEDIUM(2L, "Средний", "="),

    HIGH(3L, "Высокий", "&#129045;"),

    CRITICAL(4L, "Критический", "&#8648;");

    private final Long priority;

    private final String title;

    private final String symbol;

    TaskPriority(Long priority, String title, String symbol) {
        this.priority = priority;
        this.title = title;
        this.symbol = symbol;
    }

    public Long asLong() {
        return priority;
    }

    public String asText() {
        return title;
    }

    public String asSymbol() {
        return symbol;
    }

    public static TaskPriority of(Long priority) {
        return Stream.of(TaskPriority.values())
                .filter(taskPriority -> taskPriority.asLong().equals(priority))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

