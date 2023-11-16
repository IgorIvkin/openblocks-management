package ru.openblocks.management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.openblocks.management.abac.Abac;
import ru.openblocks.management.abac.projectaccess.ProjectAccessAccessRule;
import ru.openblocks.management.api.dto.projectaccess.get.ProjectAccessResponse;
import ru.openblocks.management.mapper.ProjectAccessMapper;
import ru.openblocks.management.persistence.entity.ProjectAccessEntity;
import ru.openblocks.management.persistence.projection.ProjectAccessProjection;
import ru.openblocks.management.persistence.repository.ProjectAccessRepository;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectAccessService {

    private final ProjectAccessRepository projectAccessRepository;

    private final ProjectAccessMapper projectAccessMapper;

    private final Clock clock = Clock.systemDefaultZone();

    @Autowired
    public ProjectAccessService(ProjectAccessRepository projectAccessRepository,
                                ProjectAccessMapper projectAccessMapper) {
        this.projectAccessRepository = projectAccessRepository;
        this.projectAccessMapper = projectAccessMapper;
    }

    /**
     * Creates access for user to a project.
     * Do nothing in case given user already has access to a project.
     *
     * @param userId      id of user
     * @param projectCode code of project
     */
    @Transactional
    @Abac(type = ProjectAccessAccessRule.class, arguments = {"projectCode"})
    public void create(Long userId, String projectCode) {
        create(userId, projectCode, false);
    }

    /**
     * Creates access for user to a project.
     * Do nothing in case given user already has access to a project.
     *
     * @param userId       id of user
     * @param projectCode  code of project
     * @param projectAdmin does user should be admin of project
     */
    @Transactional
    @Abac(type = ProjectAccessAccessRule.class, arguments = {"projectCode"})
    public void create(Long userId, String projectCode, boolean projectAdmin) {

        log.info("Add user {} access to project {}, admin status {}", userId, projectCode, projectAdmin);

        if (!projectAccessRepository.existsByUserIdAndProjectCode(userId, projectCode)) {
            ProjectAccessEntity projectAccess = new ProjectAccessEntity();
            projectAccess.setCreatedAt(Instant.now(clock));
            projectAccess.setUserId(userId);
            projectAccess.setProjectCode(projectCode);
            projectAccess.setProjectAdmin(projectAdmin);
            projectAccessRepository.save(projectAccess);
        } else {
            log.info("Not required, user already has access");
        }
    }

    /**
     * Updates access of user to a project.
     * In case if access exists then it updates its project admin status else it will
     * create an access record.
     *
     * @param userId       id of user
     * @param projectCode  code of project
     * @param projectAdmin does user should be admin of project
     */
    @Transactional
    @Abac(type = ProjectAccessAccessRule.class, arguments = {"projectCode"})
    public void update(Long userId, String projectCode, boolean projectAdmin) {

        log.info("Update user {} access to project {}, admin status {}", userId, projectCode, projectAdmin);

        ProjectAccessEntity projectAccess =
                projectAccessRepository.findByUserIdAndProjectCode(userId, projectCode).orElse(null);

        if (projectAccess != null) {
            projectAccess.setProjectAdmin(projectAdmin);
            projectAccessRepository.save(projectAccess);
        } else {
            create(userId, projectCode, projectAdmin);
        }
    }

    /**
     * Removes access of user to a project.
     *
     * @param userId      id of user
     * @param projectCode code of project
     */
    @Transactional
    @Abac(type = ProjectAccessAccessRule.class, arguments = {"projectCode"})
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

    /**
     * Checks whether a given user has access to a given task (through project of task).
     *
     * @param userId   id of user
     * @param taskCode code of task
     * @return true if user has access to a task
     */
    @Transactional(readOnly = true)
    public boolean hasAccessByTask(Long userId, String taskCode) {
        return projectAccessRepository.existsByUserIdAndTaskCode(userId, taskCode);
    }

    /**
     * Checks whether a given user has access to a given task (through link id).
     *
     * @param userId     id of user
     * @param taskLinkId id of task link
     * @return true if user has access to a task
     */
    @Transactional(readOnly = true)
    public boolean hasAccessByTaskLink(Long userId, Long taskLinkId) {
        return projectAccessRepository.existsByUserIdAndTaskLinkId(userId, taskLinkId);
    }

    /**
     * Checks whether a given user is project admin of a given project.
     *
     * @param userId      id of user
     * @param projectCode code of project
     * @return true if user has access to a project
     */
    @Transactional(readOnly = true)
    public boolean isProjectAdmin(Long userId, String projectCode) {
        return projectAccessRepository.existsByUserIdAndProjectCodeAndProjectAdmin(userId, projectCode, true);
    }

    /**
     * Returns a list of all project access by project code.
     *
     * @param projectCode code of project
     * @return all project accesses
     */
    @Transactional(readOnly = true)
    @Abac(type = ProjectAccessAccessRule.class, arguments = {"projectCode"})
    public List<ProjectAccessResponse> getByProject(String projectCode) {

        log.info("Get project accesses by code {}", projectCode);

        List<ProjectAccessProjection> projectAccesses = projectAccessRepository.findAllByProjectCode(projectCode);

        return projectAccesses.stream()
                .map(projectAccessMapper::toDto)
                .collect(Collectors.toList());
    }


}
