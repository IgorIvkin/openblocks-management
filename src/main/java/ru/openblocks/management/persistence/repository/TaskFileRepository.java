package ru.openblocks.management.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.openblocks.management.persistence.entity.TaskFileEntity;

import java.util.List;

@Repository
public interface TaskFileRepository extends JpaRepository<TaskFileEntity, Long> {

    List<TaskFileEntity> findAllByTaskCodeOrderByCreatedAtDesc(String taskCode);
}
