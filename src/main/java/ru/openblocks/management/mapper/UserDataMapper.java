package ru.openblocks.management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import ru.openblocks.management.api.dto.user.create.UserCreateRequest;
import ru.openblocks.management.api.dto.user.get.UserResponse;
import ru.openblocks.management.mapper.util.UserUtils;
import ru.openblocks.management.persistence.entity.UserDataEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserDataMapper {

    UserDataEntity toEntity(UserCreateRequest request);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "name", target = "shortName", qualifiedByName = "mapShortName")
    UserResponse toDto(UserDataEntity user);

    @Named("mapShortName")
    default String mapShortName(String name) {
        return UserUtils.mapShortName(name);
    }
}
