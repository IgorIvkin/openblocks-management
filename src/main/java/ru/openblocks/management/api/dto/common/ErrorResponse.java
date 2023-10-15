package ru.openblocks.management.api.dto.common;

import org.springframework.http.HttpStatus;

public record ErrorResponse(Integer errorCode, String message, String parameterName) {

    public ErrorResponse(Integer errorCode, String message) {
        this(errorCode, message, null);
    }

    public ErrorResponse(String message) {
        this(HttpStatus.BAD_REQUEST.value(), message, null);
    }
}
