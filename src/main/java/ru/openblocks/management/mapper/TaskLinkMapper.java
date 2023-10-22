package ru.openblocks.management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.openblocks.management.api.dto.tasklink.create.TaskLinkCreateRequest;
import ru.openblocks.management.api.dto.tasklink.get.TaskLinkResponse;
import ru.openblocks.management.persistence.entity.TaskLinkEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskLinkMapper {

    TaskLinkEntity toEntity(TaskLinkCreateRequest request);

    TaskLinkResponse toDto(TaskLinkEntity taskLink);
}
