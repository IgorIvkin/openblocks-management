package ru.openblocks.management.exception;


public class DatabaseEntityAlreadyExistsException extends RuntimeException {

    public DatabaseEntityAlreadyExistsException() {
        super();
    }

    public DatabaseEntityAlreadyExistsException(String message) {
        super(message);
    }

    public static DatabaseEntityAlreadyExistsException ofUserLogin(String login) {
        return new DatabaseEntityAlreadyExistsException("User with login " + login + " already exists");
    }

}