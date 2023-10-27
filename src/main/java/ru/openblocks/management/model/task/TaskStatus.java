package ru.openblocks.management.model.task;


import java.util.Set;
import java.util.stream.Stream;

public enum TaskStatus {

    CREATED(1L, "Создано"),

    IN_WORK(2L, "В работе"),

    CLOSED(3L, "Закрыто"),

    REJECTED(4L, "Отклонено");

    private final Long status;

    private final String title;

    TaskStatus(Long status, String title) {
        this.status = status;
        this.title = title;
    }

    public Long asLong() {
        return status;
    }

    public String asText() {
        return title;
    }

    public static TaskStatus of(Long status) {
        return Stream.of(TaskStatus.values())
                .filter(taskStatus -> taskStatus.asLong().equals(status))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    /**
     * Returns a set of statuses that recognized to be closed task.
     *
     * @return set of closed statuses
     */
    public static Set<TaskStatus> closedStatuses() {
        return Set.of(CLOSED, REJECTED);
    }
}
