package ru.openblocks.management.exception;

public class TokenRefreshException extends RuntimeException {

    public TokenRefreshException() {
        super();
    }

    public TokenRefreshException(String message) {
        super(message);
    }
}
