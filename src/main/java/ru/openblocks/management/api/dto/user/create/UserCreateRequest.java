package ru.openblocks.management.api.dto.user.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank @Size(max = 255)
        String name,

        @NotBlank @Size(max = 255)
        String login,

        @NotBlank @Size(max = 32)
        String password) {

        @Override
        public String toString() {
                return "UserCreateRequest{" +
                        "name='" + name + '\'' +
                        ", login='" + login + '\'' +
                        ", password='***'" +
                        '}';
        }
}
