package ru.openblocks.management.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.openblocks.management.model.task.TaskLinkType;
import ru.openblocks.management.persistence.entity.TaskLinkEntity;

import java.util.List;

@Repository
public interface TaskLinkRepository extends JpaRepository<TaskLinkEntity, Long> {

    List<TaskLinkEntity> getAllByTaskCode(String taskCode);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(nativeQuery = true, value = "delete from task_link tl where tl.task_code = :taskCode")
    void deleteAllByTaskCode(@Param("taskCode") String taskCode);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(nativeQuery = true, value = "delete from task_link tl " +
            "where tl.task_code = :taskCode " +
            "and tl.connected_task_code = :connectedTaskCode " +
            "and tl.link_type = :#{#linkType.asLong()}")
    void deleteALlByTaskCodeAndConnectedTaskCodeAndLinkType(@Param("taskCode") String taskCode,
                                                            @Param("connectedTaskCode") String connectedTaskCode,
                                                            @Param("linkType") TaskLinkType linkType);
}
