package ru.openblocks.management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.util.StringUtils;
import ru.openblocks.management.api.dto.backlog.get.BacklogGetResponse;
import ru.openblocks.management.mapper.util.UserUtils;
import ru.openblocks.management.persistence.projection.BacklogProjection;

import java.util.Arrays;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BacklogMapper {

    @Mapping(source = "executorId", target = "executor.id")
    @Mapping(source = "executorName", target = "executor.name")
    @Mapping(source = "executorName", target = "executor.shortName", qualifiedByName = "mapShortName")
    @Mapping(source = "sprintId", target = "sprint.id")
    @Mapping(source = "sprintTitle", target = "sprint.title")
    BacklogGetResponse toDto(BacklogProjection backlogProjection);

    @Named("mapShortName")
    default String mapShortName(String name) {
        return UserUtils.mapShortName(name);
    }
}
