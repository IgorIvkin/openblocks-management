package ru.openblocks.management.persistence.projection;


public interface ProjectAccessProjection {

    Long getUserId();

    String getProjectCode();

    Boolean getProjectAdmin();

    String getUserName();

    String getUserLogin();
}
