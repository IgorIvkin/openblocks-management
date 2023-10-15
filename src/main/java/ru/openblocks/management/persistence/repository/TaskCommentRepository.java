package ru.openblocks.management.persistence.repository;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.openblocks.management.persistence.entity.TaskCommentEntity;

import java.util.List;

@Repository
public interface TaskCommentRepository extends JpaRepository<TaskCommentEntity, Long> {

    List<TaskCommentEntity> findAllByTaskCode(@NotBlank String taskCode);
}
