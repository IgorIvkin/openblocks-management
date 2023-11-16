package ru.openblocks.management.abac.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.openblocks.management.abac.AccessRule;
import ru.openblocks.management.service.ProjectAccessService;
import ru.openblocks.management.service.UserDataService;

import java.util.Map;

@Component
public class TaskAccessRule implements AccessRule {

    private final UserDataService userDataService;

    private final ProjectAccessService projectAccessService;

    @Autowired
    public TaskAccessRule(UserDataService userDataService,
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

        Object param = arguments.get("taskCode");
        if (param == null) {
            param = arguments.get("code");
        }

        if (param != null) {
            if (param instanceof String taskCode) {
                return projectAccessService.hasAccessByTask(userId, taskCode);
            } else {
                throw new IllegalStateException("Parameters 'taskCode' or 'code' should have type String");
            }
        } else {
            throw new IllegalStateException("You have to specify parameter named 'taskCode' or 'code' " +
                    "to apply TaskAccessRule");
        }
    }
}
