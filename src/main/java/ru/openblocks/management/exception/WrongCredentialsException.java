package ru.openblocks.management.exception;

public class WrongCredentialsException extends RuntimeException {

    public WrongCredentialsException() {
        super();
    }

    public WrongCredentialsException(String message) {
        super(message);
    }
}
