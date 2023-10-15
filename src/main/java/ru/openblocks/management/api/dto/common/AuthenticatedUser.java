package ru.openblocks.management.api.dto.common;

import lombok.Builder;
import lombok.Data;
import ru.openblocks.management.api.dto.auth.UserAuthenticationInfo;

@Data
@Builder
public class AuthenticatedUser {

    private String userName;

    private UserAuthenticationInfo user;

    private String token;
}

