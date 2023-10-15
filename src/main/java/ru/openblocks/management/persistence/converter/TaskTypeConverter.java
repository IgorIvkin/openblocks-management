package ru.openblocks.management.persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.openblocks.management.model.task.TaskType;


@Converter(autoApply = true)
public class TaskTypeConverter implements AttributeConverter<TaskType, Long> {

    @Override
    public Long convertToDatabaseColumn(TaskType taskType) {
        if (taskType == null) {
            return null;
        }
        return taskType.asLong();
    }

    @Override
    public TaskType convertToEntityAttribute(Long taskTypeId) {
        if (taskTypeId == null) {
            return null;
        }
        return TaskType.of(taskTypeId);
    }
}
