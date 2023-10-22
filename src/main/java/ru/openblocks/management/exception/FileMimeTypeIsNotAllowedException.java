package ru.openblocks.management.exception;

public class FileMimeTypeIsNotAllowedException extends RuntimeException {

    public FileMimeTypeIsNotAllowedException() {
        super();
    }

    public FileMimeTypeIsNotAllowedException(String message) {
        super(message);
    }
}
