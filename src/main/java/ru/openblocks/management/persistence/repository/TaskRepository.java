package ru.openblocks.management.persistence.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.openblocks.management.model.task.TaskStatus;
import ru.openblocks.management.persistence.entity.TaskEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    Optional<TaskEntity> findByCode(String code);

    boolean existsByCode(String code);

    List<TaskEntity> findAllByProjectCode(String projectCode);

    List<TaskEntity> findAllByProjectCodeAndStatus(String projectCode, TaskStatus status);

    List<TaskEntity> findAllByExecutorId(Long executorId);

    List<TaskEntity> findAllByExecutorIdAndProjectCode(Long executorId, String projectCode);
}
