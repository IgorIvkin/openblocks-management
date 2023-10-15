package ru.openblocks.management.model.task;


import java.util.stream.Stream;

public enum TaskStatus {

    CREATED(1L),

    IN_WORK(2L),

    CLOSED(3L);

    private final Long status;

    TaskStatus(Long status) {
        this.status = status;
    }

    public Long asLong() {
        return status;
    }

    public static TaskStatus of(Long status) {
        return Stream.of(TaskStatus.values())
                .filter(taskStatus -> taskStatus.asLong().equals(status))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
