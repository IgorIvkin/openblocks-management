package ru.openblocks.management.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.openblocks.management.persistence.entity.UserDataEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDataRepository extends JpaRepository<UserDataEntity, Long> {

    Optional<UserDataEntity> findByLogin(String login);

    List<UserDataEntity> findAllByNameStartsWithIgnoreCase(String name);
}
