package ru.openblocks.management.api.dto.backlog.get;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.openblocks.management.model.task.TaskPriority;
import ru.openblocks.management.model.task.TaskStatus;
import ru.openblocks.management.model.task.TaskType;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BacklogGetResponse {

    private String taskCode;

    private String subject;

    private TaskType type;

    private TaskStatus status;

    private TaskPriority priority;

    private LocalDate dueDate;

    private Integer estimation;

    private BacklogUserGetResponse executor;
}
