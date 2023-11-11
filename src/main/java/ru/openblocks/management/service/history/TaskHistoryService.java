package ru.openblocks.management.service.history;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.openblocks.management.api.dto.taskhistory.get.TaskHistoryResponse;
import ru.openblocks.management.exception.DatabaseEntityNotFoundException;
import ru.openblocks.management.mapper.TaskHistoryMapper;
import ru.openblocks.management.model.task.TaskHistoryChangeObject;
import ru.openblocks.management.model.task.TaskPriority;
import ru.openblocks.management.model.task.TaskStatus;
import ru.openblocks.management.persistence.entity.SprintEntity;
import ru.openblocks.management.persistence.entity.TaskEntity;
import ru.openblocks.management.persistence.entity.TaskHistoryEntity;
import ru.openblocks.management.persistence.entity.UserDataEntity;
import ru.openblocks.management.persistence.repository.TaskHistoryRepository;
import ru.openblocks.management.persistence.repository.TaskRepository;
import ru.openblocks.management.service.UserDataService;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskHistoryService {

    /**
     * How much history items to return.
     */
    private static final int TASK_HISTORY_ITEMS_LIMIT = 200;

    private final UserDataService userDataService;

    private final TaskRepository taskRepository;

    private final TaskHistoryRepository taskHistoryRepository;

    private final TaskHistoryMapper taskHistoryMapper;

    private final Clock clock = Clock.systemDefaultZone();

    @Autowired
    public TaskHistoryService(UserDataService userDataService,
                              TaskRepository taskRepository,
                              TaskHistoryRepository taskHistoryRepository,
                              TaskHistoryMapper taskHistoryMapper) {
        this.userDataService = userDataService;
        this.taskRepository = taskRepository;
        this.taskHistoryRepository = taskHistoryRepository;
        this.taskHistoryMapper = taskHistoryMapper;
    }

    /**
     * Returns list of task history items by its task code.
     *
     * @param taskCode code of task
     * @return task history items
     */
    @Transactional(readOnly = true)
    public List<TaskHistoryResponse> getTaskHistory(String taskCode) {

        log.info("Get task history by task {}", taskCode);

        List<TaskHistoryEntity> history = taskHistoryRepository.findAllByTaskCode(taskCode, TASK_HISTORY_ITEMS_LIMIT, 0);

        return history.stream()
                .map(taskHistoryMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Tracks history event - create task.
     *
     * @param taskCode code of task
     */
    @Transactional
    public void trackCreateTask(String taskCode) {
        createTaskHistory(taskCode, TaskHistoryChangeObject.CREATE, null, null);
    }

    /**
     * Tracks change of task subject.
     *
     * @param taskCode      code of task
     * @param previousValue previous subject
     * @param newValue      new subject
     */
    @Transactional
    public void trackChangeSubject(String taskCode, String previousValue, String newValue) {
        if (!Objects.equals(previousValue, newValue)) {
            createTaskHistory(taskCode, TaskHistoryChangeObject.SUBJECT, previousValue, newValue);
        }
    }

    /**
     * Tracks change of task explanation.
     *
     * @param taskCode      code of task
     * @param previousValue previous explanation
     * @param newValue      new explanation
     */
    @Transactional
    public void trackChangeExplanation(String taskCode, String previousValue, String newValue) {
        if (!Objects.equals(previousValue, newValue)) {
            createTaskHistory(taskCode, TaskHistoryChangeObject.EXPLANATION, previousValue, newValue);
        }
    }

    /**
     * Tracks change of task status.
     *
     * @param taskCode      code of task
     * @param previousValue previous task status
     * @param newValue      new task status
     */
    @Transactional
    public void trackChangeTaskStatus(String taskCode, TaskStatus previousValue, TaskStatus newValue) {
        if (!Objects.equals(previousValue, newValue)) {
            createTaskHistory(
                    taskCode,
                    TaskHistoryChangeObject.TASK_STATUS,
                    previousValue.asText(),
                    newValue.asText()
            );
        }
    }

    /**
     * Tracks change of task priority.
     *
     * @param taskCode      code of task
     * @param previousValue previous task priority
     * @param newValue      new task priority
     */
    @Transactional
    public void trackChangeTaskPriority(String taskCode, TaskPriority previousValue, TaskPriority newValue) {
        if (!Objects.equals(previousValue, newValue)) {
            createTaskHistory(
                    taskCode,
                    TaskHistoryChangeObject.TASK_PRIORITY,
                    previousValue.asText(),
                    newValue.asText()
            );
        }
    }

    /**
     * Tracks change of task due date.
     *
     * @param taskCode      code of task
     * @param previousValue previous due date
     * @param newValue      new due date
     */
    @Transactional
    public void trackChangeTaskDueDate(String taskCode, LocalDate previousValue, LocalDate newValue) {
        if (!Objects.equals(previousValue, newValue)) {
            createTaskHistory(
                    taskCode,
                    TaskHistoryChangeObject.DUE_DATE,
                    previousValue.format(DateTimeFormatter.ISO_DATE),
                    newValue.format(DateTimeFormatter.ISO_DATE)
            );
        }
    }

    /**
     * Tracks change of task estimation
     *
     * @param taskCode      code of task
     * @param previousValue previous task estimation
     * @param newValue      new task estimation
     */
    @Transactional
    public void trackChangeEstimation(String taskCode, Integer previousValue, Integer newValue) {
        if (!Objects.equals(previousValue, newValue)) {
            createTaskHistory(
                    taskCode,
                    TaskHistoryChangeObject.ESTIMATION,
                    previousValue.toString(),
                    newValue.toString()
            );
        }
    }

    /**
     * Tracks change of task executor
     *
     * @param taskCode      code of task
     * @param previousValue previous executor
     * @param newValue      new executor
     */
    @Transactional
    public void trackChangeExecutor(String taskCode,
                                    UserDataEntity previousValue,
                                    UserDataEntity newValue) {
        createTaskHistory(
                taskCode,
                TaskHistoryChangeObject.EXECUTOR,
                buildUserInfo(previousValue),
                buildUserInfo(newValue)
        );
    }

    /**
     * Tracks change of task owner
     *
     * @param taskCode      code of task
     * @param previousValue previous owner
     * @param newValue      new owner
     */
    @Transactional
    public void trackChangeOwner(String taskCode,
                                 UserDataEntity previousValue,
                                 UserDataEntity newValue) {
        createTaskHistory(
                taskCode,
                TaskHistoryChangeObject.OWNER,
                buildUserInfo(previousValue),
                buildUserInfo(newValue)
        );
    }

    /**
     * Tracks change of task sprint.
     *
     * @param taskCode      code of task
     * @param previousValue previous sprint
     * @param newValue      new sprint
     */
    @Transactional
    public void trackChangeSprint(String taskCode,
                                  SprintEntity previousValue,
                                  SprintEntity newValue) {
        createTaskHistory(
                taskCode,
                TaskHistoryChangeObject.SPRINT,
                buildSprintInfo(previousValue),
                buildSprintInfo(newValue)
        );
    }

    private String buildUserInfo(UserDataEntity user) {
        if (user != null) {
            return user.getName() + " (" + user.getLogin() + ")";
        }
        return null;
    }

    private String buildSprintInfo(SprintEntity sprint) {
        if (sprint != null) {
            return sprint.getTitle();
        }
        return null;
    }

    private void createTaskHistory(String taskCode,
                                   TaskHistoryChangeObject changeObject,
                                   String previousValue,
                                   String newValue) {

        log.info("Track history change, task code {}, object {}, previous value {}, new value {}", taskCode,
                changeObject, previousValue, newValue);

        final UserDataEntity author = userDataService.getCurrentUser();
        final TaskEntity task = taskRepository.findByCode(taskCode)
                .orElseThrow(() -> DatabaseEntityNotFoundException.ofTaskCode(taskCode));

        TaskHistoryEntity taskHistory = new TaskHistoryEntity();

        taskHistory.setCreatedAt(Instant.now(clock));
        taskHistory.setAuthor(author);
        taskHistory.setTask(task);
        taskHistory.setChangeObject(changeObject);
        taskHistory.setPreviousValue(previousValue);
        taskHistory.setNewValue(newValue);

        taskHistoryRepository.save(taskHistory);
    }
}
