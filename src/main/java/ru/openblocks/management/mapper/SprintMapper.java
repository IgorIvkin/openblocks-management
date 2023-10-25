package ru.openblocks.management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.openblocks.management.api.dto.sprint.create.SprintCreateRequest;
import ru.openblocks.management.api.dto.sprint.get.SprintGetResponse;
import ru.openblocks.management.persistence.entity.SprintEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SprintMapper {

    SprintEntity toEntity(SprintCreateRequest request);

    SprintGetResponse toDto(SprintEntity sprint);
}
