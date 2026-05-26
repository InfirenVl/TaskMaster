package com.infiren.taskmaster.repository;

import com.infiren.taskmaster.object.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {

    //@Query("select t from TaskEntity t where t.assignedUserId = ?1 and t.status = ?2")
    List<TaskEntity> findByAssignedUserIdAndStatus(Integer assignedUserId, TaskEntity.Status status);

//    @Query("""
//            update TaskEntity t set t.status = :status
//            where t.id = :id
//            """)
//    void setStatus(@Param("id") Integer id, @Param("status")TaskEntity.Status status);

//    @Query("select t from TaskEntity t where t.assignedUserId = ?1")
//    List<TaskEntity> findByAssignedUserId(Integer assignedUserId);
}
