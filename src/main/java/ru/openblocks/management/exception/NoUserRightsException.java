package ru.openblocks.management.exception;

public class NoUserRightsException extends RuntimeException {

    public NoUserRightsException() {
        super();
    }

    public NoUserRightsException(String message) {
        super(message);
    }

    public static NoUserRightsException anonymousForbidden() {
        return new NoUserRightsException("This action is not available for anonymous user");
    }

    public static NoUserRightsException notEnoughRights() {
        return new NoUserRightsException("You have not enough rights to manipulate with object");
    }

    public static NoUserRightsException noAccessToProject(String code) {
        return new NoUserRightsException("User has no rights to access project: " + code);
    }

    public static NoUserRightsException ofEntityId(String type, Long entityId) {
        return new NoUserRightsException("You have not enough rights to manipulate with object " + type + " with ID "
                + entityId);
    }
}
