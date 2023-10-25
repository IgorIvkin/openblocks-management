package ru.openblocks.management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.openblocks.management.api.dto.sprint.create.SprintCreateRequest;
import ru.openblocks.management.api.dto.sprint.get.SprintGetResponse;
import ru.openblocks.management.exception.DatabaseEntityAlreadyExistsException;
import ru.openblocks.management.exception.DatabaseEntityNotFoundException;
import ru.openblocks.management.mapper.SprintMapper;
import ru.openblocks.management.persistence.entity.ProjectEntity;
import ru.openblocks.management.persistence.entity.SprintEntity;
import ru.openblocks.management.persistence.repository.ProjectRepository;
import ru.openblocks.management.persistence.repository.SprintRepository;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SprintService {

    private final SprintRepository sprintRepository;

    private final ProjectRepository projectRepository;

    private final SprintMapper sprintMapper;

    private final Clock clock = Clock.systemDefaultZone();

    @Autowired
    public SprintService(SprintRepository sprintRepository,
                         ProjectRepository projectRepository,
                         SprintMapper sprintMapper) {
        this.sprintRepository = sprintRepository;
        this.projectRepository = projectRepository;
        this.sprintMapper = sprintMapper;
    }

    /**
     * Creates a new sprint.
     * Checks that new sprint will not overlap existing sprints for a given project.
     *
     * @param request request to create new sprint
     * @return id of new sprint
     */
    @Transactional
    public Long create(SprintCreateRequest request) {

        log.info("Create sprint by request {}", request);

        final String projectCode = request.projectCode();
        final LocalDate startDate = request.startDate();
        final LocalDate endDate = request.endDate();

        ProjectEntity project = projectRepository.findByCode(projectCode)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofProjectCode(projectCode));

        // Checks if sprint overlaps existing sprints
        List<SprintEntity> sprints = sprintRepository.findAllByProjectCodeAndStartDate(projectCode, startDate);
        if (sprints.stream()
                .anyMatch(sprint -> overlaps(startDate, endDate, sprint))) {
            throw DatabaseEntityAlreadyExistsException.ofOverlappingSprints(projectCode, startDate, endDate);
        }

        SprintEntity sprint = sprintMapper.toEntity(request);
        sprint.setProject(project);
        sprint.setCreatedAt(Instant.now(clock));
        sprint.setFinished(false);

        SprintEntity savedSprint = sprintRepository.save(sprint);
        return savedSprint.getId();
    }

    /**
     * Returns all sprints by project code.
     *
     * @param projectCode code of project
     * @return sprints by project code
     */
    @Transactional(readOnly = true)
    public List<SprintGetResponse> getByProject(String projectCode) {

        log.info("Get all sprints by project {}", projectCode);

        projectRepository.findByCode(projectCode)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofProjectCode(projectCode));

        List<SprintEntity> sprints = sprintRepository.findAllByProjectCode(projectCode);

        return sprints.stream()
                .map(sprintMapper::toDto)
                .collect(Collectors.toList());
    }

    private boolean overlaps(LocalDate startDate,
                             LocalDate endDate,
                             SprintEntity sprint) {
        return (startDate.isEqual(sprint.getEndDate()) || startDate.isBefore(sprint.getEndDate()))
                        && (endDate.isEqual(sprint.getStartDate()) || endDate.isAfter(sprint.getStartDate()));
    }
}
