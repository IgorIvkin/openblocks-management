package ru.openblocks.management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.openblocks.management.api.dto.project.get.ProjectResponse;
import ru.openblocks.management.exception.DatabaseEntityNotFoundException;
import ru.openblocks.management.mapper.ProjectMapper;
import ru.openblocks.management.persistence.entity.ProjectEntity;
import ru.openblocks.management.persistence.repository.ProjectRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    @Autowired
    public ProjectService(ProjectRepository projectRepository,
                          ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
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
