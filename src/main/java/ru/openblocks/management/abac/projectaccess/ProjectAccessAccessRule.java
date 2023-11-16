package ru.openblocks.management.abac.projectaccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.openblocks.management.abac.AccessRule;
import ru.openblocks.management.persistence.entity.UserDataEntity;
import ru.openblocks.management.service.ProjectAccessService;
import ru.openblocks.management.service.UserDataService;

import java.util.Map;

@Component
public class ProjectAccessAccessRule implements AccessRule {

    private final UserDataService userDataService;

    private final ProjectAccessService projectAccessService;

    @Autowired
    public ProjectAccessAccessRule(UserDataService userDataService,
                             ProjectAccessService projectAccessService) {
        this.userDataService = userDataService;
        this.projectAccessService = projectAccessService;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean check(Map<String, Object> arguments) {

        final UserDataEntity currentUser = userDataService.getCurrentUser();
        final Long userId = getUserId(currentUser);
        if (userId == null) {
            throw new IllegalStateException("User should be logged to access this check");
        }

        final Object param = arguments.get("projectCode");
        if (param != null) {
            if (param instanceof String projectCode) {
                return userDataService.isCurrentUserAdmin()
                        || projectAccessService.isProjectAdmin(userId, projectCode);
            } else {
                throw new IllegalStateException("Parameter 'projectCode' should have type String");
            }
        } else {
            throw new IllegalStateException("You have to specify parameter named 'projectCode' " +
                    "to apply ProjectAccessAccessRule");
        }
    }
}
