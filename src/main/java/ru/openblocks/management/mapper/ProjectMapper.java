package ru.openblocks.management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.openblocks.management.api.dto.project.create.ProjectCreateRequest;
import ru.openblocks.management.api.dto.project.get.ProjectResponse;
import ru.openblocks.management.persistence.entity.ProjectEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectMapper {

    ProjectResponse toDto(ProjectEntity project);

    ProjectEntity toEntity(ProjectCreateRequest request);
}
