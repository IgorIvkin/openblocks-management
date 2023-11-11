package ru.openblocks.management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.openblocks.management.api.dto.taskhistory.get.TaskHistoryResponse;
import ru.openblocks.management.persistence.entity.TaskHistoryEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskHistoryMapper {

    TaskHistoryResponse toDto(TaskHistoryEntity history);
}
