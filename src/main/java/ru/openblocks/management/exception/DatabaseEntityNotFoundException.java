package ru.openblocks.management.exception;

public class DatabaseEntityNotFoundException extends RuntimeException {

    public DatabaseEntityNotFoundException() {
        super();
    }

    public DatabaseEntityNotFoundException(String message) {
        super(message);
    }

    public static DatabaseEntityNotFoundException ofUserId(Long id) {
        return new DatabaseEntityNotFoundException("User with id " + id + " was not found");
    }

    public static DatabaseEntityNotFoundException ofUserLogin(String login) {
        return new DatabaseEntityNotFoundException("User with login " + login + " was not found");
    }

    public static DatabaseEntityNotFoundException ofRoleId(Long id) {
        return new DatabaseEntityNotFoundException("User role with id " + id + " was not found");
    }

    public static DatabaseEntityNotFoundException ofProjectCode(String code) {
        return new DatabaseEntityNotFoundException("Project with code " + code + " was not found");
    }

    public static DatabaseEntityNotFoundException ofTaskCode(String code) {
        return new DatabaseEntityNotFoundException("Task with code " + code + " was not found");
    }

    public static DatabaseEntityNotFoundException ofTaskCommentId(Long id) {
        return new DatabaseEntityNotFoundException("Task comment with id " + id + " was not found");
    }

    public static DatabaseEntityNotFoundException ofTaskLinkId(Long id) {
        return new DatabaseEntityNotFoundException("Task link with id " + id + " was not found");
    }

    public static DatabaseEntityNotFoundException ofTaskFileId(Long id) {
        return new DatabaseEntityNotFoundException("Task file with id " + id + " was not found");
    }
}
