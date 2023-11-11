package ru.openblocks.management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.openblocks.management.api.dto.project.create.ProjectCreateRequest;
import ru.openblocks.management.api.dto.project.get.ProjectResponse;
import ru.openblocks.management.exception.DatabaseEntityAlreadyExistsException;
import ru.openblocks.management.exception.DatabaseEntityNotFoundException;
import ru.openblocks.management.mapper.ProjectMapper;
import ru.openblocks.management.persistence.entity.ProjectEntity;
import ru.openblocks.management.persistence.repository.ProjectRepository;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectService {

    private final ProjectAccessService projectAccessService;

    private final UserDataService userDataService;

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    private final Clock clock = Clock.systemDefaultZone();

    @Autowired
    public ProjectService(ProjectAccessService projectAccessService,
                          UserDataService userDataService,
                          ProjectRepository projectRepository,
                          ProjectMapper projectMapper) {
        this.projectAccessService = projectAccessService;
        this.userDataService = userDataService;
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    /**
     * Creates a new project.
     * Gives also access to this project to current user.
     *
     * @param request request to create new project
     */
    @Transactional
    public void create(ProjectCreateRequest request) {

        log.info("Create new project by request {}", request);

        final String projectCode = request.code();
        if (projectRepository.existsByCode(projectCode)) {
            throw DatabaseEntityAlreadyExistsException.ofProjectCode(projectCode);
        }

        ProjectEntity project = projectMapper.toEntity(request);
        project.setCreatedAt(Instant.now(clock));
        project.setTaskCounter(0L);

        projectRepository.save(project);

        // Give access to current user to the newly created project
        final Long userId = userDataService.getCurrentUserId();
        projectAccessService.create(userId, projectCode);
    }

    /**
     * Returns the project by its given code.
     *
     * @param code code of project
     * @return project by its code
     */
    @Transactional(readOnly = true)
    public ProjectResponse getByCode(String code) {

        log.info("Get project by code {}", code);

        ProjectEntity project = projectRepository.findByCode(code)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofProjectCode(code));

        return projectMapper.toDto(project);
    }

    /**
     * Returns all existing projects.
     *
     * @return all existing projects
     */
    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjects() {

        log.info("Get all projects");

        List<ProjectEntity> projects = projectRepository.findAll();

        return projects.stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Increases and returns a number representing a count of tasks by project by its given code.
     *
     * @param code project code
     * @return next number for a task
     */
    @Transactional
    public Long getNewTaskNumberByCode(String code) {

        log.info("Get new task number by code {}", code);

        projectRepository.findByCode(code)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofProjectCode(code));

        Long currentTaskCounter = projectRepository.incrementAndGetByCode(code);
        if (currentTaskCounter == null) {
            throw new IllegalStateException("Cannot increase task counter for project: " + code);
        }
        return currentTaskCounter;
    }
}
