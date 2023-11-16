package ru.openblocks.management.abac.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.openblocks.management.abac.AccessRule;
import ru.openblocks.management.service.ProjectAccessService;
import ru.openblocks.management.service.UserDataService;

import java.util.Map;

@Component
public class TaskLinkDeleteAccessRule implements AccessRule {

    private final UserDataService userDataService;

    private final ProjectAccessService projectAccessService;

    @Autowired
    public TaskLinkDeleteAccessRule(UserDataService userDataService,
                                    ProjectAccessService projectAccessService) {
        this.userDataService = userDataService;
        this.projectAccessService = projectAccessService;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean check(Map<String, Object> arguments) {

        final Long userId = userDataService.getCurrentUserId();
        if (userId == null) {
            throw new IllegalStateException("User should be logged to access this check");
        }

        Object param = arguments.get("id");
        if (param != null) {
            if (param instanceof Long taskLinkId) {
                return projectAccessService.hasAccessByTaskLink(userId, taskLinkId);
            } else {
                throw new IllegalStateException("Parameter 'id' should have type Long");
            }
        } else {
            throw new IllegalStateException("You have to specify parameter named 'id' " +
                    "to apply TaskLinkDeleteAccessRule");
        }
    }
}
