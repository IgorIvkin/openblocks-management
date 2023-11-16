package ru.openblocks.management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.openblocks.management.api.dto.projectaccess.get.ProjectAccessResponse;
import ru.openblocks.management.persistence.projection.ProjectAccessProjection;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectAccessMapper {

    @Mapping(source = "userName", target = "user.name")
    @Mapping(source = "userLogin", target = "user.login")
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "userId", target = "userId")
    ProjectAccessResponse toDto(ProjectAccessProjection projectAccess);
}
