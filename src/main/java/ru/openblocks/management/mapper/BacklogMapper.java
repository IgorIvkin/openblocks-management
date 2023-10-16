package ru.openblocks.management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.util.StringUtils;
import ru.openblocks.management.api.dto.backlog.get.BacklogGetResponse;
import ru.openblocks.management.persistence.projection.BacklogProjection;

import java.util.Arrays;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BacklogMapper {

    @Mapping(source = "executorId", target = "executor.id")
    @Mapping(source = "executorName", target = "executor.name")
    @Mapping(source = "executorName", target = "executor.shortName", qualifiedByName = "mapShortName")
    BacklogGetResponse toDto(BacklogProjection backlogProjection);

    @Named("mapShortName")
    default String mapShortName(String name) {
        if (name != null) {
            return Arrays.stream(name.split(" "))
                    .filter(StringUtils::hasText)
                    .limit(2)
                    .map(item -> item.substring(0, 1).toUpperCase())
                    .collect(Collectors.joining(""));
        }
        return null;
    }
}
