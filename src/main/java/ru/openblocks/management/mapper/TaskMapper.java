package ru.openblocks.management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.util.StringUtils;
import ru.openblocks.management.api.dto.task.create.TaskCreateRequest;
import ru.openblocks.management.api.dto.task.get.TaskCardResponse;
import ru.openblocks.management.mapper.util.UserUtils;
import ru.openblocks.management.persistence.entity.TaskEntity;

import java.util.Arrays;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {

    TaskEntity toEntity(TaskCreateRequest request);

    @Mapping(source = "task.owner.name", target = "owner.name")
    @Mapping(source = "task.owner.name", target = "owner.shortName", qualifiedByName = "mapShortName")
    @Mapping(source = "task.executor.name", target = "executor.name")
    @Mapping(source = "task.executor.name", target = "executor.shortName", qualifiedByName = "mapShortName")
    TaskCardResponse toDto(TaskEntity task);

    @Named("mapShortName")
    default String mapShortName(String name) {
        return UserUtils.mapShortName(name);
    }
}
