package ru.openblocks.management.persistence.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.openblocks.management.api.dto.backlog.get.BacklogGetRequest;
import ru.openblocks.management.model.task.TaskPriority;
import ru.openblocks.management.model.task.TaskStatus;
import ru.openblocks.management.persistence.projection.BacklogProjection;
import ru.openblocks.management.persistence.util.NativeQueryUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class BacklogRepository {

    private final EntityManager entityManager;

    @Autowired
    public BacklogRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    public List<BacklogProjection> getBacklog(BacklogGetRequest request) {
        Query query = entityManager.createNativeQuery(buildBacklogQuery(request), Tuple.class);

        NativeQueryUtils.setParameterIfPresent(query, "projectCode", request::projectCode);
        NativeQueryUtils.setParameterIfPresent(query, "executorId", request::executorId);
        NativeQueryUtils.setParameterIfPresent(query, "subject", () -> "%" + request.subject() + "%");
        NativeQueryUtils.setParameterIfPresent(query, "priorities", request::priorities);

        final List<Tuple> resultList = query.getResultList();

        return resultList.stream()
                .map(this::mapTuple)
                .collect(Collectors.toList());
    }

    private String buildBacklogQuery(BacklogGetRequest request) {
        return "select  " +
                "  t.code as taskCode, " +
                "  t.status as status, " +
                "  t.task_type as type, " +
                "  t.priority as priority, " +
                "  t.subject as subject, " +
                "  t.due_date as dueDate, " +
                "  t.estimation as estimation, " +
                "  t.executor_id as executorId, " +
                "  ud.name as executorName " +
                "from task t  " +
                "left join user_data ud on ud.id = t.executor_id  " +
                "where 1=1 " +
                buildProjectCodeFilter(request) +
                buildExecutorIdFilter(request) +
                buildSubjectFilter(request) +
                buildStatusesFilter(request) +
                buildPrioritiesFilter(request) +
                buildOrderBySection(request);
    }

    private String buildProjectCodeFilter(BacklogGetRequest request) {
        final String projectCode = request.projectCode();
        if (projectCode != null) {
            return " and t.project_code = :projectCode ";
        }
        return "";
    }

    private String buildExecutorIdFilter(BacklogGetRequest request) {
        final Long executorId = request.executorId();
        if (executorId != null) {
            return " and t.executor_id = :executorId ";
        }
        return "";
    }

    private String buildSubjectFilter(BacklogGetRequest request) {
        final String subject = request.subject();
        if (subject != null && subject.length() >= 2) {
            return " and t.subject like :subject ";
        }
        return "";
    }

    private String buildStatusesFilter(BacklogGetRequest request) {
        final Set<TaskStatus> statuses = request.statuses();
        if (statuses != null && !statuses.isEmpty()) {
            return " and t.status in (" +
                    statuses.stream()
                            .map(status -> status.asLong().toString())
                            .collect(Collectors.joining(",")) +
                    ") ";
        }
        return "";
    }

    private String buildPrioritiesFilter(BacklogGetRequest request) {
        final Set<TaskPriority> priorities = request.priorities();
        if (priorities != null && !priorities.isEmpty()) {
            return " and t.priority in (:priorities) ";
        }
        return "";
    }

    private String buildOrderBySection(BacklogGetRequest request) {
        return " order by t.code desc ";
    }

    private BacklogProjection mapTuple(Tuple tuple) {
        return BacklogProjection.builder()
                .taskCode(NativeQueryUtils.mapString("taskCode", tuple))
                .subject(NativeQueryUtils.mapString("subject", tuple))
                .type(NativeQueryUtils.mapType("type", tuple))
                .status(NativeQueryUtils.mapStatus("status", tuple))
                .priority(NativeQueryUtils.mapPriority("priority", tuple))
                .dueDate(NativeQueryUtils.mapLocalDate("dueDate", tuple))
                .estimation(NativeQueryUtils.mapInteger("estimation", tuple))
                .executorId(NativeQueryUtils.mapLong("executorId", tuple))
                .executorName(NativeQueryUtils.mapString("executorName", tuple))
                .build();
    }
}