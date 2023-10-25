package ru.openblocks.management.persistence.projection;

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
public class BacklogProjection {

    private String taskCode;

    private String subject;

    private TaskType type;

    private TaskStatus status;

    private TaskPriority priority;

    private LocalDate dueDate;

    private Long executorId;

    private String executorName;

    private Integer estimation;

    private Long sprintId;

    private String sprintTitle;
}
