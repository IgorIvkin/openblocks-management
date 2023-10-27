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
import ru.openblocks.management.model.task.TaskStatus;
import ru.openblocks.management.persistence.entity.ProjectEntity;
import ru.openblocks.management.persistence.entity.SprintEntity;
import ru.openblocks.management.persistence.entity.TaskEntity;
import ru.openblocks.management.persistence.repository.ProjectRepository;
import ru.openblocks.management.persistence.repository.SprintRepository;
import ru.openblocks.management.persistence.repository.TaskRepository;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SprintService {

    private final SprintRepository sprintRepository;

    private final ProjectRepository projectRepository;

    private final TaskRepository taskRepository;

    private final SprintMapper sprintMapper;

    private final Clock clock = Clock.systemDefaultZone();

    @Autowired
    public SprintService(SprintRepository sprintRepository,
                         ProjectRepository projectRepository,
                         TaskRepository taskRepository,
                         SprintMapper sprintMapper) {
        this.sprintRepository = sprintRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.sprintMapper = sprintMapper;
    }

    /**
     * Creates a new sprint.
     * Checks that new sprint will not overlap existing sprints for a given project.
     * Checks that project doesn't contain sprint with the same title, it's forbidden also.
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
        final String title = request.title();

        ProjectEntity project = projectRepository.findByCode(projectCode)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofProjectCode(projectCode));

        // Checks if sprint overlaps existing sprints
        List<SprintEntity> sprints = sprintRepository.findAllByProjectCodeAndStartDate(projectCode, startDate);
        if (sprints.stream()
                .anyMatch(sprint -> overlaps(startDate, endDate, sprint))) {
            throw DatabaseEntityAlreadyExistsException.ofOverlappingSprints(projectCode, startDate, endDate);
        }

        // Checks if this project already has sprint with the same title
        if (sprints.stream()
                .anyMatch(sprint -> Objects.equals(title, sprint.getTitle()))) {
            throw DatabaseEntityAlreadyExistsException.ofSprintWithSameTitle(projectCode, title);
        }

        SprintEntity sprint = sprintMapper.toEntity(request);
        sprint.setProject(project);
        sprint.setCreatedAt(Instant.now(clock));
        sprint.setFinished(false);

        SprintEntity savedSprint = sprintRepository.save(sprint);
        return savedSprint.getId();
    }

    /**
     * Closes an existing sprint. All the unfinished tasks from this sprint are moved to backlog.
     * Sprint is marked as finished and is no more available to select to assign task to sprint.
     * This operation is unrecoverable, you will not be able to return sprint back to live.
     *
     * @param sprintId id of sprint
     */
    @Transactional
    public void closeSprint(Long sprintId) {

        log.info("Close sprint by id {}", sprintId);

        SprintEntity sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofSprintId(sprintId));

        // Close sprint
        sprint.setFinished(true);

        // Move all unclosed tasks to backlog (removing sprint)
        final List<TaskEntity> tasksBySprint = taskRepository.findAllBySprintId(sprintId);
        final Set<TaskStatus> taskStatuses = TaskStatus.closedStatuses();

        for (TaskEntity task : tasksBySprint) {
            if (!taskStatuses.contains(task.getStatus())) {
                log.info("Move task {} from sprint {} to backlog because task is unfinished", task.getCode(), sprintId);
                task.setSprint(null);
                task.setUpdatedAt(Instant.now(clock));
            }
        }

        sprintRepository.save(sprint);
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

        if (!projectRepository.existsByCode(projectCode)) {
            throw DatabaseEntityNotFoundException.ofProjectCode(projectCode);
        }

        List<SprintEntity> sprints = sprintRepository.findAllByProjectCode(projectCode);

        return sprints.stream()
                .map(sprintMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Returns all unfinished sprints by project code.
     *
     * @param projectCode code of project
     * @return unfinished sprints by project code
     */
    @Transactional(readOnly = true)
    public List<SprintGetResponse> getByProjectUnfinished(String projectCode) {

        log.info("Get all unfinished sprints by project {}", projectCode);

        if (!projectRepository.existsByCode(projectCode)) {
            throw DatabaseEntityNotFoundException.ofProjectCode(projectCode);
        }

        List<SprintEntity> sprints = sprintRepository.findAllUnfinishedByProjectCode(projectCode);

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
