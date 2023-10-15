package ru.openblocks.management.persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.openblocks.management.model.task.TaskStatus;


@Converter(autoApply = true)
public class TaskStatusConverter implements AttributeConverter<TaskStatus, Long> {

    @Override
    public Long convertToDatabaseColumn(TaskStatus taskStatus) {
        if (taskStatus == null) {
            return null;
        }
        return taskStatus.asLong();
    }

    @Override
    public TaskStatus convertToEntityAttribute(Long taskStatusId) {
        if (taskStatusId == null) {
            return null;
        }
        return TaskStatus.of(taskStatusId);
    }
}

