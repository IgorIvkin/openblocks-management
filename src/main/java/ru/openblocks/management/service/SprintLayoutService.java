package ru.openblocks.management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.openblocks.management.abac.Abac;
import ru.openblocks.management.abac.project.ProjectAccessRule;
import ru.openblocks.management.api.dto.sprintlayout.get.SprintLayoutResponse;
import ru.openblocks.management.api.dto.sprintlayout.update.SprintLayoutUpdateRequest;
import ru.openblocks.management.exception.DatabaseEntityNotFoundException;
import ru.openblocks.management.persistence.entity.ProjectEntity;
import ru.openblocks.management.persistence.entity.SprintEntity;
import ru.openblocks.management.persistence.entity.SprintLayoutEntity;
import ru.openblocks.management.persistence.repository.ProjectRepository;
import ru.openblocks.management.persistence.repository.SprintLayoutRepository;
import ru.openblocks.management.persistence.repository.SprintRepository;

import java.util.List;

@Slf4j
@Service
public class SprintLayoutService {

    private final SprintLayoutRepository sprintLayoutRepository;

    private final SprintRepository sprintRepository;

    private final ProjectRepository projectRepository;

    @Autowired
    public SprintLayoutService(SprintLayoutRepository sprintLayoutRepository,
                               SprintRepository sprintRepository,
                               ProjectRepository projectRepository) {
        this.sprintLayoutRepository = sprintLayoutRepository;
        this.sprintRepository = sprintRepository;
        this.projectRepository = projectRepository;
    }

    /**
     * Updates a layout of sprint: remembers the order of tasks in sprint.
     *
     * @param projectCode code of project
     * @param request     request to update layout of sprint
     */
    @Transactional
    @Abac(type = ProjectAccessRule.class, arguments = {"projectCode"})
    public void updateLayout(String projectCode, SprintLayoutUpdateRequest request) {

        log.info("Update project {} sprint layout by request {}", projectCode, request);

        final Long sprintId = request.sprintId();

        if (!sprintRepository.existsById(sprintId)) {
            throw DatabaseEntityNotFoundException.ofSprintId(sprintId);
        }

        sprintLayoutRepository.findBySprintId(sprintId)
                .ifPresentOrElse(
                        layout -> updateSprintLayout(layout, request),
                        () -> createSprintLayout(projectCode, request)
                );
    }

    /**
     * Returns a sprint layout by its given ID.
     *
     * @param sprintId ID of sprint
     * @return layout of sprint
     */
    @Transactional(readOnly = true)
    public SprintLayoutResponse getLayout(Long sprintId) {

        log.info("Get sprint layout by sprint {}", sprintId);

        if (!sprintRepository.existsById(sprintId)) {
            throw DatabaseEntityNotFoundException.ofSprintId(sprintId);
        }

        return sprintLayoutRepository.findBySprintId(sprintId)
                .map(layout -> new SprintLayoutResponse(sprintId, layout.getSprintLayout()))
                .orElse(new SprintLayoutResponse(sprintId, List.of()));
    }

    private void updateSprintLayout(SprintLayoutEntity sprintLayout, SprintLayoutUpdateRequest request) {
        log.info("Sprint layout already exists, will update it");
        sprintLayout.setSprintLayout(request.sprintLayout());
        sprintLayoutRepository.save(sprintLayout);
    }

    private void createSprintLayout(String projectCode, SprintLayoutUpdateRequest request) {
        log.info("Sprint layout is not presented, will create it");

        final Long sprintId = request.sprintId();
        final ProjectEntity project = projectRepository.findByCode(projectCode)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofProjectCode(projectCode));
        final SprintEntity sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofSprintId(sprintId));

        SprintLayoutEntity sprintLayout = new SprintLayoutEntity();
        sprintLayout.setProject(project);
        sprintLayout.setSprint(sprint);
        sprintLayout.setSprintLayout(request.sprintLayout());

        sprintLayoutRepository.save(sprintLayout);
    }
}
