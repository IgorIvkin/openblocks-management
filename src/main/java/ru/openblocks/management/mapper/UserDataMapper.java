package ru.openblocks.management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.openblocks.management.api.dto.user.create.UserCreateRequest;
import ru.openblocks.management.api.dto.user.get.UserResponse;
import ru.openblocks.management.persistence.entity.UserDataEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserDataMapper {

    UserDataEntity toEntity(UserCreateRequest request);

    UserResponse toDto(UserDataEntity user);
}
