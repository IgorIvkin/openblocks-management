package ru.openblocks.management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.openblocks.management.api.dto.taskcomment.create.TaskCommentCreateRequest;
import ru.openblocks.management.api.dto.taskcomment.get.TaskCommentResponse;
import ru.openblocks.management.persistence.entity.TaskCommentEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskCommentMapper {

    TaskCommentEntity toEntity(TaskCommentCreateRequest request);

    TaskCommentResponse toDto(TaskCommentEntity task);
}
