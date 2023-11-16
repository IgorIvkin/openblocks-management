package ru.openblocks.management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.openblocks.management.abac.Abac;
import ru.openblocks.management.abac.task.TaskAccessRule;
import ru.openblocks.management.api.dto.taskcomment.create.TaskCommentCreateRequest;
import ru.openblocks.management.api.dto.taskcomment.get.TaskCommentResponse;
import ru.openblocks.management.api.dto.taskcomment.update.TaskCommentUpdateRequest;
import ru.openblocks.management.exception.DatabaseEntityNotFoundException;
import ru.openblocks.management.mapper.TaskCommentMapper;
import ru.openblocks.management.persistence.entity.TaskCommentEntity;
import ru.openblocks.management.persistence.entity.UserDataEntity;
import ru.openblocks.management.persistence.repository.TaskCommentRepository;
import ru.openblocks.management.persistence.repository.TaskRepository;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskCommentService {

    private final UserDataService userDataService;

    private final TaskRepository taskRepository;

    private final TaskCommentRepository taskCommentRepository;

    private final TaskCommentMapper taskCommentMapper;

    private final Clock clock = Clock.systemDefaultZone();

    @Autowired
    public TaskCommentService(UserDataService userDataService,
                              TaskRepository taskRepository,
                              TaskCommentRepository taskCommentRepository,
                              TaskCommentMapper taskCommentMapper) {
        this.userDataService = userDataService;
        this.taskRepository = taskRepository;
        this.taskCommentRepository = taskCommentRepository;
        this.taskCommentMapper = taskCommentMapper;
    }

    /**
     * Creates a comment for a task by its given code.
     *
     * @param code    code of task
     * @param request request to create comment
     * @return ID of newly created comment
     */
    @Transactional
    @Abac(type = TaskAccessRule.class, arguments = {"code"})
    public Long create(String code, TaskCommentCreateRequest request) {

        log.info("Add comment {} for task {}", request, code);

        taskRepository.findByCode(code)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofTaskCode(code));
        final UserDataEntity author = userDataService.getCurrentUser();
        validateUser(author);

        TaskCommentEntity taskComment = taskCommentMapper.toEntity(request);
        taskComment.setCreatedAt(Instant.now(clock));
        taskComment.setTaskCode(code);
        taskComment.setAuthor(author);

        TaskCommentEntity createdTaskComment = taskCommentRepository.save(taskComment);
        return createdTaskComment.getId();
    }

    /**
     * Updates an existing comment by its ID.
     *
     * @param id      id of comment
     * @param request request to update comment
     */
    @Transactional
    public void update(Long id, TaskCommentUpdateRequest request) {

        log.info("Update task comment by ID {} with content {}", id, request);

        final TaskCommentEntity taskComment = taskCommentRepository.findById(id)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofTaskCommentId(id));

        taskComment.setContent(request.content());
        taskCommentRepository.save(taskComment);
    }

    /**
     * Deletes a comment by its ID.
     *
     * @param id id of comment
     */
    @Transactional
    public void delete(Long id) {

        log.info("Delete task comment by ID {}", id);

        taskCommentRepository.deleteById(id);
    }

    /**
     * Returns a task comment by its given ID.
     *
     * @param id id of task comment
     * @return task comment
     */
    @Transactional(readOnly = true)
    public TaskCommentResponse getById(Long id) {

        log.info("Get task comment by ID {}", id);

        final TaskCommentEntity taskComment = taskCommentRepository.findById(id)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofTaskCommentId(id));

        return taskCommentMapper.toDto(taskComment);
    }

    /**
     * Returns all task comments by a given task represented by its code.
     *
     * @param code code of task
     * @return task comments
     */
    @Transactional(readOnly = true)
    @Abac(type = TaskAccessRule.class, arguments = {"code"})
    public List<TaskCommentResponse> getByTaskCode(String code) {

        log.info("Get comments by task {}", code);

        taskRepository.findByCode(code)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofTaskCode(code));

        List<TaskCommentEntity> taskComments = taskCommentRepository.findAllByTaskCode(code);

        return taskComments.stream()
                .map(taskCommentMapper::toDto)
                .collect(Collectors.toList());
    }

    private void validateUser(UserDataEntity user) {
        if (user == null) {
            throw new IllegalStateException("Cannot manipulate with task comments, user is null");
        }
    }
}
