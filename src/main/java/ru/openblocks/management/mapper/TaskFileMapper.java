package ru.openblocks.management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.openblocks.management.api.dto.taskfile.get.TaskFileGetResponse;
import ru.openblocks.management.persistence.entity.TaskFileEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskFileMapper {

    TaskFileGetResponse toDto(TaskFileEntity file);
}
