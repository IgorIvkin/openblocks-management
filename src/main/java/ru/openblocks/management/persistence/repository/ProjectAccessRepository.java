package ru.openblocks.management.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.openblocks.management.persistence.entity.ProjectAccessEntity;
import ru.openblocks.management.persistence.projection.ProjectAccessProjection;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectAccessRepository extends JpaRepository<ProjectAccessEntity, Long> {

    Optional<ProjectAccessEntity> findByUserIdAndProjectCode(Long userId, String projectCode);

    @Query(nativeQuery = true,
            value = "select  " +
                    "  pa.user_id as userId, " +
                    "  pa.project_code as projectCode, " +
                    "  pa.project_admin as projectAdmin, " +
                    "  ud.name as userName, " +
                    "  ud.login as userLogin " +
                    "from project_access pa " +
                    "inner join user_data ud on ud.id = pa.user_id where pa.project_code = :projectCode " +
                    "order by pa.user_id desc")
    List<ProjectAccessProjection> findAllByProjectCode(@Param("projectCode") String projectCode);

    boolean existsByUserIdAndProjectCode(Long userId, String projectCode);

    boolean existsByUserIdAndProjectCodeAndProjectAdmin(Long userId,
                                                        String projectCode,
                                                        boolean projectAdmin);

    @Query(nativeQuery = true,
            value = "select exists( " +
                    "  select * from task t  " +
                    "  inner join project_access pa on pa.user_id = :userId and pa.project_code = t.project_code  " +
                    "  where t.code = :taskCode " +
                    ");")
    boolean existsByUserIdAndTaskCode(@Param("userId") Long userId,
                                      @Param("taskCode") String taskCode);

    @Query(nativeQuery = true,
            value = "select exists( " +
                    "  select * from task_link tl " +
                    "  inner join task t on t.code = tl.task_code  " +
                    "  inner join project_access pa on pa.user_id = :userId and pa.project_code = t.project_code  " +
                    "  where tl.id = :taskLinkId " +
                    ")")
    boolean existsByUserIdAndTaskLinkId(@Param("userId") Long userId,
                                        @Param("taskLinkId") Long taskLinkId);

    void deleteAllByUserIdAndProjectCode(Long userId, String projectCode);
}
