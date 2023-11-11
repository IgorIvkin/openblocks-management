package ru.openblocks.management.model.task;

import java.util.stream.Stream;

public enum TaskHistoryChangeObject {

    CREATE(1L, "Задача создана"),

    SUBJECT(2L, "Название задачи"),

    EXPLANATION(3L, "Описание задачи"),

    TASK_STATUS(4L, "Статус задачи"),

    TASK_PRIORITY(5L, "Приоритет задачи"),

    DUE_DATE(6L, "Срок завершения"),

    ESTIMATION(7L, "Оценка в днях"),

    EXECUTOR(8L, "Исполнитель"),

    OWNER(9L, "Владелец"),

    SPRINT(10L, "Спринт");

    private final Long changeId;

    private final String title;

    TaskHistoryChangeObject(Long changeId, String title) {
        this.changeId = changeId;
        this.title = title;
    }

    public Long asLong() {
        return changeId;
    }

    public String asText() {
        return title;
    }

    public static TaskHistoryChangeObject of(Long id) {
        return Stream.of(TaskHistoryChangeObject.values())
                .filter(change -> change.asLong().equals(id))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
