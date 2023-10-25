package ru.openblocks.management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.openblocks.management.api.dto.tasklink.create.TaskLinkCreateRequest;
import ru.openblocks.management.api.dto.tasklink.get.TaskLinkResponse;
import ru.openblocks.management.exception.DatabaseEntityAlreadyExistsException;
import ru.openblocks.management.exception.DatabaseEntityNotFoundException;
import ru.openblocks.management.mapper.TaskLinkMapper;
import ru.openblocks.management.model.task.TaskLinkType;
import ru.openblocks.management.persistence.entity.TaskEntity;
import ru.openblocks.management.persistence.entity.TaskLinkEntity;
import ru.openblocks.management.persistence.repository.TaskLinkRepository;
import ru.openblocks.management.persistence.repository.TaskRepository;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskLinkService {

    private final TaskRepository taskRepository;

    private final TaskLinkRepository taskLinkRepository;

    private final TaskLinkMapper taskLinkMapper;

    private final Clock clock = Clock.systemDefaultZone();

    @Autowired
    public TaskLinkService(TaskRepository taskRepository,
                           TaskLinkRepository taskLinkRepository,
                           TaskLinkMapper taskLinkMapper) {
        this.taskRepository = taskRepository;
        this.taskLinkRepository = taskLinkRepository;
        this.taskLinkMapper = taskLinkMapper;
    }

    /**
     * Creates task link by code of main task and connected task and also by link type.
     * Creates also second-side link.
     *
     * @param request request to create task link
     * @return ID of created link
     */
    @Transactional
    public Long create(TaskLinkCreateRequest request) {

        log.info("Create task link by request {}", request);

        final String taskCode = request.taskCode();
        final String connectedTaskCode = request.connectedTaskCode();
        final TaskLinkType linkType = request.linkType();
        final TaskLinkType connectedLinkType = getConnectedLinkType(linkType);

        // Fails if both tasks are the same
        if (Objects.equals(taskCode, connectedTaskCode)) {
            throw DatabaseEntityAlreadyExistsException.ofTaskLink(taskCode);
        }

        // Check that both tasks are exist
        TaskEntity task = taskRepository.findByCode(taskCode)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofTaskCode(taskCode));
        TaskEntity connectedTask = taskRepository.findByCode(connectedTaskCode)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofTaskCode(connectedTaskCode));

        // Fails if such link already exists for a given tasks
        if (taskLinkRepository.getAllByTaskCode(taskCode)
                .stream()
                .anyMatch(taskLink -> byConnectedTaskCodeAndLinkType(taskLink, connectedTaskCode, linkType))) {
            throw DatabaseEntityAlreadyExistsException.ofTaskLink(taskCode, connectedTaskCode, linkType);
        }

        // Create both links
        final Instant now = Instant.now(clock);
        TaskLinkEntity leftLink = TaskLinkEntity.builder()
                .task(task)
                .connectedTask(connectedTask)
                .linkType(linkType)
                .createdAt(now)
                .build();
        TaskLinkEntity rightLink = TaskLinkEntity.builder()
                .task(connectedTask)
                .connectedTask(task)
                .linkType(connectedLinkType)
                .createdAt(now)
                .build();
        TaskLinkEntity createdLeftLink = taskLinkRepository.save(leftLink);
        taskLinkRepository.save(rightLink);

        return createdLeftLink.getId();
    }

    /**
     * Returns a list of task links by code of task.
     *
     * @param taskCode code of task
     * @return list of task links
     */
    @Transactional(readOnly = true)
    public List<TaskLinkResponse> getByTaskCode(String taskCode) {

        log.info("Get task links by code {}", taskCode);

        List<TaskLinkEntity> taskLinks = taskLinkRepository.getAllByTaskCode(taskCode);

        return taskLinks.stream()
                .map(taskLinkMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Deletes task link by its given ID.
     * Deletes also second-side link.
     *
     * @param id ID of link to delete
     */
    @Transactional
    public void deleteById(Long id) {

        log.info("Delete task link by id {}", id);

        TaskLinkEntity taskLink = taskLinkRepository.findById(id)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofTaskLinkId(id));

        final String taskCode = taskLink.getTask().getCode();
        final String connectedTaskCode = taskLink.getConnectedTask().getCode();
        final TaskLinkType linkType = taskLink.getLinkType();
        final TaskLinkType connectedLinkType = getConnectedLinkType(linkType);

        log.info("Delete links between {} and {} by type {}", connectedTaskCode, taskCode, linkType);
        taskLinkRepository.deleteById(id);
        taskLinkRepository.deleteALlByTaskCodeAndConnectedTaskCodeAndLinkType(
                connectedTaskCode,
                taskCode,
                connectedLinkType
        );

    }

    private boolean byConnectedTaskCodeAndLinkType(TaskLinkEntity taskLink,
                                                   String connectedTaskCode,
                                                   TaskLinkType linkType) {
        if (taskLink != null && taskLink.getConnectedTask() != null) {
            return Objects.equals(taskLink.getConnectedTask().getCode(), connectedTaskCode)
                    && Objects.equals(taskLink.getLinkType(), linkType);
        }
        return false;
    }

    private TaskLinkType getConnectedLinkType(TaskLinkType linkType) {
        return switch (linkType) {
            case ASSOCIATED -> TaskLinkType.ASSOCIATED;
            case PARENT_OF -> TaskLinkType.CHILD_OF;
            case CHILD_OF -> TaskLinkType.PARENT_OF;
        };
    }
}
