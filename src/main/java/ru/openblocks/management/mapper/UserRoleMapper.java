package ru.openblocks.management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.openblocks.management.api.dto.userrole.create.UserRoleCreateRequest;
import ru.openblocks.management.api.dto.userrole.get.UserRoleResponse;
import ru.openblocks.management.api.dto.userrole.update.UserRoleUpdateRequest;
import ru.openblocks.management.persistence.entity.UserRoleEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserRoleMapper {

    UserRoleEntity toEntity(UserRoleCreateRequest request);

    void updateEntity(@MappingTarget UserRoleEntity userRole, UserRoleUpdateRequest request);

    UserRoleResponse toDto(UserRoleEntity userRole);
}
