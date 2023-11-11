package ru.openblocks.management.persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.openblocks.management.model.task.TaskHistoryChangeObject;

@Converter(autoApply = true)
public class TaskHistoryChangeObjectConverter implements AttributeConverter<TaskHistoryChangeObject, Long> {

    @Override
    public Long convertToDatabaseColumn(TaskHistoryChangeObject changeObject) {
        if (changeObject == null) {
            return null;
        }
        return changeObject.asLong();
    }

    @Override
    public TaskHistoryChangeObject convertToEntityAttribute(Long changeId) {
        if (changeId == null) {
            return null;
        }
        return TaskHistoryChangeObject.of(changeId);
    }
}
