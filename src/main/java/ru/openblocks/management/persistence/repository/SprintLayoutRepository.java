package ru.openblocks.management.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.openblocks.management.persistence.entity.SprintLayoutEntity;

import java.util.Optional;

@Repository
public interface SprintLayoutRepository extends JpaRepository<SprintLayoutEntity, Long> {

    Optional<SprintLayoutEntity> findBySprintId(Long sprintId);
}
