package ru.openblocks.management.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.openblocks.management.persistence.entity.UserPositionEntity;
import ru.openblocks.management.persistence.entity.UserPositionId;

import java.util.List;

@Repository
public interface UserPositionRepository extends JpaRepository<UserPositionEntity, UserPositionId> {

    List<UserPositionEntity> findAllByUserId(Long userId);
}
