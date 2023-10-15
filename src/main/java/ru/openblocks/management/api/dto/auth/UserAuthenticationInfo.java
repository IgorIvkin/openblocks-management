package ru.openblocks.management.api.dto.auth;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthenticationInfo {

    private Long id;

    private String name;

    private String login;

    private Instant createdAt;
}
