package ru.openblocks.management.persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.openblocks.management.model.task.TaskPriority;

@Converter(autoApply = true)
public class TaskPriorityConverter implements AttributeConverter<TaskPriority, Long> {

    @Override
    public Long convertToDatabaseColumn(TaskPriority taskPriority) {
        if (taskPriority == null) {
            return null;
        }
        return taskPriority.asLong();
    }

    @Override
    public TaskPriority convertToEntityAttribute(Long taskPriorityId) {
        if (taskPriorityId == null) {
            return null;
        }
        return TaskPriority.of(taskPriorityId);
    }
}
