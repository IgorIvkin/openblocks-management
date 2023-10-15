package ru.openblocks.management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.openblocks.management.api.dto.task.create.TaskCreateRequest;
import ru.openblocks.management.api.dto.task.get.TaskCardResponse;
import ru.openblocks.management.persistence.entity.TaskEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {

    TaskEntity toEntity(TaskCreateRequest request);

    TaskCardResponse toDto(TaskEntity task);
}
