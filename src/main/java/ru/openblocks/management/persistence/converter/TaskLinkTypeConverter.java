package ru.openblocks.management.persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.openblocks.management.model.task.TaskLinkType;

@Converter(autoApply = true)
public class TaskLinkTypeConverter implements AttributeConverter<TaskLinkType, Long> {

    @Override
    public Long convertToDatabaseColumn(TaskLinkType taskLinkType) {
        if (taskLinkType == null) {
            return null;
        }
        return taskLinkType.asLong();
    }

    @Override
    public TaskLinkType convertToEntityAttribute(Long taskLinkTypeId) {
        if (taskLinkTypeId == null) {
            return null;
        }
        return TaskLinkType.of(taskLinkTypeId);
    }
}