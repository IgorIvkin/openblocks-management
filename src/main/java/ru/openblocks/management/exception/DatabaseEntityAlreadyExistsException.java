package ru.openblocks.management.exception;


import ru.openblocks.management.model.task.TaskLinkType;

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

    public static DatabaseEntityAlreadyExistsException ofTaskLink(String taskCode,
                                                                  String connectedTaskCode,
                                                                  TaskLinkType linkType) {
        return new DatabaseEntityAlreadyExistsException("Task link " + linkType + " already exists for " + taskCode
                + " and " + connectedTaskCode);
    }

}