package ru.openblocks.management.abac;

import ru.openblocks.management.api.dto.common.AuthenticatedUser;
import ru.openblocks.management.persistence.entity.UserDataEntity;

import java.util.Map;
import java.util.Optional;

public interface AccessRule {

    boolean check(Map<String, Object> arguments);

    default Long getUserId(UserDataEntity user) {
        return Optional.ofNullable(user)
                .map(UserDataEntity::getId)
                .orElse(null);
    }
}
