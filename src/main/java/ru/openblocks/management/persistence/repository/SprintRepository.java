package ru.openblocks.management.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.openblocks.management.persistence.entity.SprintEntity;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SprintRepository extends JpaRepository<SprintEntity, Long> {

    List<SprintEntity> findAllByProjectCode(String projectCode);

    List<SprintEntity> findAllByProjectCodeAndTitle(String projectCode, String title);

    @Query(nativeQuery = true,
            value = "select * from sprint s " +
                    "where s.project_code = :projectCode and s.start_date >= :startDate " +
                    "order by s.start_date asc")
    List<SprintEntity> findAllByProjectCodeAndStartDate(@Param("projectCode") String projectCode,
                                                        @Param("startDate") LocalDate startDate);

    @Query(nativeQuery = true,
            value = "select * from sprint s " +
                    "where s.project_code = :projectCode and s.finished <> true " +
                    "order by s.start_date asc")
    List<SprintEntity> findAllUnfinishedByProjectCode(@Param("projectCode") String projectCode);
}
