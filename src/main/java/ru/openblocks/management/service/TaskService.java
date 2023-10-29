package ru.openblocks.management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.openblocks.management.abac.Abac;
import ru.openblocks.management.abac.task.ProjectAccessRule;
import ru.openblocks.management.api.dto.task.create.TaskCreateRequest;
import ru.openblocks.management.api.dto.task.get.TaskCardResponse;
import ru.openblocks.management.api.dto.task.update.*;
import ru.openblocks.management.exception.DatabaseEntityNotFoundException;
import ru.openblocks.management.mapper.TaskMapper;
import ru.openblocks.management.model.task.TaskPriority;
import ru.openblocks.management.model.task.TaskStatus;
import ru.openblocks.management.persistence.entity.ProjectEntity;
import ru.openblocks.management.persistence.entity.SprintEntity;
import ru.openblocks.management.persistence.entity.TaskEntity;
import ru.openblocks.management.persistence.entity.UserDataEntity;
import ru.openblocks.management.persistence.repository.ProjectRepository;
import ru.openblocks.management.persistence.repository.SprintRepository;
import ru.openblocks.management.persistence.repository.TaskRepository;
import ru.openblocks.management.persistence.repository.UserDataRepository;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class TaskService {

    private final UserDataService userDataService;

    private final TaskRepository taskRepository;

    private final ProjectRepository projectRepository;

    private final UserDataRepository userDataRepository;

    private final SprintRepository sprintRepository;

    private final TaskMapper taskMapper;

    private final Clock clock = Clock.systemDefaultZone();

    @Autowired
    public TaskService(UserDataService userDataService,
                       ProjectRepository projectRepository,
                       TaskRepository taskRepository,
                       UserDataRepository userDataRepository,
                       SprintRepository sprintRepository,
                       TaskMapper taskMapper) {
        this.userDataService = userDataService;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.userDataRepository = userDataRepository;
        this.sprintRepository = sprintRepository;
        this.taskMapper = taskMapper;
    }

    /**
     * Creates a new task. A task is defined by its project and code of the project is mandatory field
     * to create new task. Every project has its own counter of tasks and newly created task will have a code
     * in format "PROJECT-nnn". For example having a project with code ABC and third task created in it
     * will result with code ABC-3.
     *
     * @param projectCode code of project
     * @param request     request to create a task
     * @return code of the task
     */
    @Transactional
    @Abac(type = ProjectAccessRule.class, arguments = {"projectCode"})
    public String create(String projectCode, TaskCreateRequest request) {

        log.info("Create task by request {}", request);

        // Get current project for a task
        final ProjectEntity project = projectRepository.findByCode(projectCode)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofProjectCode(projectCode));

        final SprintEntity sprint =
                Optional.ofNullable(request.getSprintId())
                        .flatMap(sprintRepository::findById)
                        .orElse(null);

        // Get executor of the task
        final UserDataEntity executor = getUserById(request.getExecutorId());

        // Get owner of the task. If it's not defined then current user is used.
        final UserDataEntity owner =
                Optional.ofNullable(getUserById(request.getOwnerId()))
                .orElse(userDataService.getCurrentUser());

        // Create a new task
        final String taskCode = createNewTaskCode(projectCode);
        final Instant now = Instant.now(clock);
        TaskEntity task = taskMapper.toEntity(request);
        task.setProject(project);
        task.setOwner(owner);
        task.setExecutor(executor);
        task.setCreatedAt(now);
        task.setUpdatedAt(now);
        task.setStatus(TaskStatus.CREATED);
        task.setCode(taskCode);
        task.setSprint(sprint);

        taskRepository.save(task);
        log.info("Created a task with code {}", taskCode);
        return taskCode;
    }

    /**
     * Clones a given task copying a set of fields to a new task.
     * New task is inside the same project and sprint by default.
     *
     * @param taskCode task code to copy
     * @return task code of new task
     */
    @Transactional
    public String cloneTask(String taskCode) {

        log.info("Clone task {}", taskCode);

        final TaskEntity original = taskRepository.findByCode(taskCode)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofTaskCode(taskCode));

        // Clones a task fields to a new task
        TaskEntity newTask = cloneOriginalTask(original);

        taskRepository.save(newTask);
        final String newTaskCode = newTask.getCode();
        log.info("Cloned a task with code {}", newTaskCode);
        return newTaskCode;
    }

    /**
     * Returns a task card by code.
     *
     * @param code code of task
     * @return card of task
     */
    @Transactional(readOnly = true)
    public TaskCardResponse getByCode(String code) {

        log.info("Get task by code {}", code);

        TaskEntity task = taskRepository.findByCode(code)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofTaskCode(code));

        return taskMapper.toDto(task);
    }

    /**
     * Changes a status of a task by its given code.
     *
     * @param code    code of task
     * @param request request to update status of task
     */
    @Transactional
    public void updateTaskStatus(String code, TaskUpdateStatusRequest request) {

        log.info("Change status of task {} to {}", code, request);

        final TaskStatus status = request.status();
        TaskEntity task = taskRepository.findByCode(code)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofTaskCode(code));

        if (!Objects.equals(status, task.getStatus())) {
            final Instant now = Instant.now(clock);
            task.setStatus(status);
            task.setUpdatedAt(now);
            taskRepository.save(task);
        }
    }

    /**
     * Updates a subject of task by its given code.
     *
     * @param code    code of task
     * @param request request to update subject of task
     */
    @Transactional
    public void updateTaskSubject(String code, TaskUpdateSubjectRequest request) {

        log.info("Change subject of task {} to {}", code, request);

        final String subject = request.subject();
        TaskEntity task = taskRepository.findByCode(code)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofTaskCode(code));

        if (!Objects.equals(subject, task.getSubject())) {
            final Instant now = Instant.now(clock);
            task.setSubject(subject);
            task.setUpdatedAt(now);
            taskRepository.save(task);
        }
    }

    /**
     * Updates an explanation of task by its given code.
     *
     * @param code    code of task
     * @param request request to update explanation of task
     */
    @Transactional
    public void updateTaskExplanation(String code, TaskUpdateExplanationRequest request) {

        log.info("Change explanation of task {} to {}", code, request);

        final String explanation = request.explanation();
        TaskEntity task = taskRepository.findByCode(code)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofTaskCode(code));

        if (!Objects.equals(explanation, task.getExplanation())) {
            final Instant now = Instant.now(clock);
            task.setExplanation(explanation);
            task.setUpdatedAt(now);
            taskRepository.save(task);
        }
    }

    /**
     * Updates a due date of task by its given code.
     *
     * @param code    code of task
     * @param request request to update due date of task
     */
    @Transactional
    public void updateTaskDueDate(String code, TaskUpdateDueDateRequest request) {

        log.info("Change due date of task {} to {}", code, request);

        final LocalDate dueDate = request.dueDate();
        TaskEntity task = taskRepository.findByCode(code)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofTaskCode(code));

        if (!Objects.equals(dueDate, task.getDueDate())) {
            final Instant now = Instant.now(clock);
            task.setDueDate(dueDate);
            task.setUpdatedAt(now);
            taskRepository.save(task);
        }
    }

    /**
     * Updates an estimation of task by its given code.
     *
     * @param code    code of task
     * @param request request to update estimation of task
     */
    @Transactional
    public void updateTaskEstimation(String code, TaskUpdateEstimationRequest request) {

        log.info("Change estimation of task {} to {}", code, request);

        final Integer estimation = request.estimation();
        TaskEntity task = taskRepository.findByCode(code)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofTaskCode(code));

        if (!Objects.equals(estimation, task.getEstimation())) {
            final Instant now = Instant.now(clock);
            task.setEstimation(estimation);
            task.setUpdatedAt(now);
            taskRepository.save(task);
        }
    }

    /**
     * Updates a priority of task by its given code.
     *
     * @param code    code of task
     * @param request request to update priority of task
     */
    @Transactional
    public void updateTaskPriority(String code, TaskUpdatePriorityRequest request) {

        log.info("Change priority of task {} to {}", code, request);

        final TaskPriority priority = request.priority();
        TaskEntity task = taskRepository.findByCode(code)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofTaskCode(code));

        if (!Objects.equals(priority, task.getPriority())) {
            final Instant now = Instant.now(clock);
            task.setPriority(priority);
            task.setUpdatedAt(now);
            taskRepository.save(task);
        }
    }

    /**
     * Updates an owner of the task by its given code.
     *
     * @param code    code of task
     * @param request request to update owner
     */
    @Transactional
    public void updateTaskOwner(String code, TaskUpdateOwnerRequest request) {

        log.info("Change owner of task {} to {}", code, request);

        final Long ownerId = request.ownerId();
        final UserDataEntity owner = getUserById(ownerId);
        TaskEntity task = taskRepository.findByCode(code)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofTaskCode(code));

        if (!Objects.equals(ownerId, getOwnerIdFromTask(task))) {
            final Instant now = Instant.now(clock);
            task.setOwner(owner);
            task.setUpdatedAt(now);
            taskRepository.save(task);
        }
    }

    /**
     * Updates an executor of the task by its given code.
     *
     * @param code    code of task
     * @param request request to update executor
     */
    @Transactional
    public void updateTaskExecutor(String code, TaskUpdateExecutorRequest request) {

        log.info("Change executor of task {} to {}", code, request);

        final Long executorId = request.executorId();
        final UserDataEntity executor = getUserById(executorId);
        TaskEntity task = taskRepository.findByCode(code)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofTaskCode(code));

        if (!Objects.equals(executorId, getExecutorIdFromTask(task))) {
            final Instant now = Instant.now(clock);
            task.setExecutor(executor);
            task.setUpdatedAt(now);
            taskRepository.save(task);
        }
    }

    /**
     * Updates sprint of task by its given code.
     *
     * @param code    code of task
     * @param request request to update sprint
     */
    @Transactional
    public void updateTaskSprint(String code, TaskUpdateSprintRequest request) {

        log.info("Change spring of task {} to {}", code, request);

        final Long sprintId = request.sprintId();
        final SprintEntity sprint =
                Optional.ofNullable(sprintId)
                .flatMap(sprintRepository::findById)
                .orElse(null);

        TaskEntity task = taskRepository.findByCode(code)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofTaskCode(code));

        if (!Objects.equals(sprintId, getSprintIdFromTask(task))) {
            final Instant now = Instant.now(clock);
            task.setSprint(sprint);
            task.setUpdatedAt(now);
            taskRepository.save(task);
        }
    }

    private String createNewTaskCode(String projectCode) {
        final Long newTaskId = projectRepository.incrementAndGetByCode(projectCode);
        if (newTaskId == null) {
            throw new IllegalStateException("Cannot increase task counter for project: " + projectCode);
        }
        return projectCode.toUpperCase() + "-" + newTaskId;
    }

    private Long getExecutorIdFromTask(TaskEntity task) {
        return Optional.ofNullable(task.getExecutor())
                .map(UserDataEntity::getId)
                .orElse(null);
    }

    private Long getOwnerIdFromTask(TaskEntity task) {
        return Optional.ofNullable(task.getOwner())
                .map(UserDataEntity::getId)
                .orElse(null);
    }

    private Long getSprintIdFromTask(TaskEntity task) {
        return Optional.ofNullable(task.getSprint())
                .map(SprintEntity::getId)
                .orElse(null);
    }

    private UserDataEntity getUserById(Long userId) {
        if (userId != null) {
            return userDataRepository.findById(userId)
                    .orElseThrow(() -> DatabaseEntityNotFoundException.ofUserId(userId));
        }
        return null;
    }

    private TaskEntity cloneOriginalTask(TaskEntity original) {

        final String projectCode = original.getProject().getCode();
        final String newTaskCode = createNewTaskCode(projectCode);
        final Instant now = Instant.now(clock);

        TaskEntity newTask = new TaskEntity();
        newTask.setCode(newTaskCode);
        newTask.setCreatedAt(now);
        newTask.setUpdatedAt(now);
        newTask.setStatus(TaskStatus.CREATED);
        newTask.setSubject(original.getSubject());
        newTask.setExplanation(original.getExplanation());
        newTask.setProject(original.getProject());
        newTask.setExecutor(original.getExecutor());
        newTask.setOwner(original.getOwner());
        newTask.setSprint(original.getSprint());
        newTask.setPriority(original.getPriority());
        newTask.setTaskType(original.getTaskType());
        newTask.setDueDate(original.getDueDate());
        newTask.setEstimation(original.getEstimation());
        return newTask;
    }
}
