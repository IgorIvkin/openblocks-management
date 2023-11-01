package ru.openblocks.management.api.dto.backlog.get;

import jakarta.validation.constraints.Size;
import ru.openblocks.management.model.task.TaskPriority;
import ru.openblocks.management.model.task.TaskStatus;
import ru.openblocks.management.model.task.TaskType;

import java.time.LocalDate;
import java.util.Set;

public record BacklogGetRequest(
        Long executorId,

        Long ownerId,

        @Size(max = 255) String projectCode,

        @Size(max = 255) String taskCode,

        @Size(min = 2, max = 255) String subject,

        Set<TaskStatus> statuses,

        Set<TaskPriority> priorities,

        Set<TaskType> taskTypes,

        Set<Long> sprints,

        LocalDate dateFrom,

        LocalDate dateTo,

        BacklogGetOrderByRequest orderBy
) {
}
