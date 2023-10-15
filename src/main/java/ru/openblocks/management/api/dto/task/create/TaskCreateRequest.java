package ru.openblocks.management.api.dto.task.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.openblocks.management.model.task.TaskType;

import java.time.LocalDate;

@Data
@Builder
public class TaskCreateRequest {

    @NotBlank
    @Size(max = 255)
    private String subject;

    @NotBlank
    @Size(max = 10000)
    private String explanation;

    @NotNull
    private TaskType taskType;

    private Long ownerId;

    private Long executorId;

    private Integer estimation;

    private LocalDate dueDate;
}
