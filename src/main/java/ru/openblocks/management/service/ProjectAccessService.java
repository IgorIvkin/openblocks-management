package ru.openblocks.management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.openblocks.management.persistence.entity.ProjectAccessEntity;
import ru.openblocks.management.persistence.repository.ProjectAccessRepository;

import java.time.Clock;
import java.time.Instant;

@Slf4j
@Service
public class ProjectAccessService {

    private final ProjectAccessRepository projectAccessRepository;

    private final Clock clock = Clock.systemDefaultZone();

    @Autowired
    public ProjectAccessService(ProjectAccessRepository projectAccessRepository) {
        this.projectAccessRepository = projectAccessRepository;
    }

    /**
     * Creates access for user to a project.
     * Do nothing in case given user already has access to a project.
     *
     * @param userId      id of user
     * @param projectCode code of project
     */
    @Transactional
    public void create(Long userId, String projectCode) {

        log.info("Add user {} access to project {}", userId, projectCode);

        if (!projectAccessRepository.existsByUserIdAndProjectCode(userId, projectCode)) {
            ProjectAccessEntity projectAccess = new ProjectAccessEntity();
            projectAccess.setCreatedAt(Instant.now(clock));
            projectAccess.setUserId(userId);
            projectAccess.setProjectCode(projectCode);
            projectAccessRepository.save(projectAccess);
        } else {
            log.info("Not required, user already has access");
        }
    }

    /**
     * Removes access of user to a project.
     *
     * @param userId      id of user
     * @param projectCode code of project
     */
    @Transactional
    public void delete(Long userId, String projectCode) {

        log.info("Remove access of user {} to project {}", userId, projectCode);

        projectAccessRepository.deleteAllByUserIdAndProjectCode(userId, projectCode);
    }

    /**
     * Checks whether a given user has access to a given project.
     *
     * @param userId      id of user
     * @param projectCode code of project
     * @return true if user has access to a project
     */
    @Transactional(readOnly = true)
    public boolean hasAccess(Long userId, String projectCode) {
        return projectAccessRepository.existsByUserIdAndProjectCode(userId, projectCode);
    }

}
