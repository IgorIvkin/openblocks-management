package ru.openblocks.management.exception;


import ru.openblocks.management.model.task.TaskLinkType;

import java.time.LocalDate;

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

    public static DatabaseEntityAlreadyExistsException ofTaskLink(String taskCode) {
        return new DatabaseEntityAlreadyExistsException("Cannot create link for itself by task code: " + taskCode);
    }

    public static DatabaseEntityAlreadyExistsException ofOverlappingSprints(String projectCode,
                                                                            LocalDate startDate,
                                                                            LocalDate endDate) {
        return new DatabaseEntityAlreadyExistsException("Spring for project " + projectCode + " overlaps with dates "
                + startDate + " and " + endDate);
    }

}