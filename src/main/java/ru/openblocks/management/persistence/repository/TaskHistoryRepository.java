package ru.openblocks.management.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.openblocks.management.persistence.entity.TaskHistoryEntity;

import java.util.List;

@Repository
public interface TaskHistoryRepository extends JpaRepository<TaskHistoryEntity, Long> {

    @Query(nativeQuery = true, value = "select * from task_history th " +
            "where th.task_code = :taskCode order by th.created_at desc limit :limit offset :offset")
    List<TaskHistoryEntity> findAllByTaskCode(@Param("taskCode") String taskCode,
                                              @Param("limit") int limit,
                                              @Param("offset") int offset);
}
