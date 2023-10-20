package ru.openblocks.management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.openblocks.management.api.dto.reference.get.TaskPriorityReferenceResponse;
import ru.openblocks.management.api.dto.reference.get.TaskStatusReferenceResponse;
import ru.openblocks.management.api.dto.reference.get.TaskTypeReferenceResponse;
import ru.openblocks.management.model.task.TaskPriority;
import ru.openblocks.management.model.task.TaskStatus;
import ru.openblocks.management.model.task.TaskType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReferenceService {

    /**
     * Returns a list of task statuses.
     *
     * @return task statuses
     */
    public List<TaskStatusReferenceResponse> getStatuses() {
        return Arrays.stream(TaskStatus.values())
                .map(this::mapStatus)
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of task priorities.
     *
     * @return task priorities
     */
    public List<TaskPriorityReferenceResponse> getPriorities() {
        return Arrays.stream(TaskPriority.values())
                .map(this::mapPriority)
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of task types.
     *
     * @return task types
     */
    public List<TaskTypeReferenceResponse> getTypes() {
        return Arrays.stream(TaskType.values())
                .map(this::mapType)
                .collect(Collectors.toList());
    }

    private TaskStatusReferenceResponse mapStatus(TaskStatus status) {
        return new TaskStatusReferenceResponse(status, status.asLong(), status.asText());
    }

    private TaskPriorityReferenceResponse mapPriority(TaskPriority priority) {
        return new TaskPriorityReferenceResponse(priority, priority.asLong(), priority.asText(), priority.asSymbol());
    }

    private TaskTypeReferenceResponse mapType(TaskType type) {
        return new TaskTypeReferenceResponse(type, type.asLong(), "");
    }
}
