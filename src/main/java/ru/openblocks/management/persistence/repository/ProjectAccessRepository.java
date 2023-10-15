package ru.openblocks.management.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.openblocks.management.persistence.entity.ProjectAccessEntity;

@Repository
public interface ProjectAccessRepository extends JpaRepository<ProjectAccessEntity, Long> {

    boolean existsByUserIdAndProjectCode(Long userId, String projectCode);

    void deleteAllByUserIdAndProjectCode(Long userId, String projectCode);
}
