package ru.openblocks.management.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.openblocks.management.persistence.entity.ProjectEntity;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    boolean existsByCode(String code);

    Optional<ProjectEntity> findByCode(String code);

    @Query(nativeQuery = true,
            value = "update project  " +
                    "set task_counter = task_counter + 1 " +
                    "where code = :code  " +
                    "returning task_counter")
    Long incrementAndGetByCode(@Param("code") String code);
}
