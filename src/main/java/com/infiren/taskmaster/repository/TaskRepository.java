package com.infiren.taskmaster.repository;

import com.infiren.taskmaster.object.entity.TaskEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {

    @Query("select t from TaskEntity t where t.assignedUserId = ?1")
    List<TaskEntity> findByAssignedUserId(Integer assignedUserId);

    //@Query("select t from TaskEntity t where t.assignedUserId = ?1 and t.status = ?2")
    List<TaskEntity> findByAssignedUserIdAndStatus(Integer assignedUserId, TaskEntity.Status status);

//    @Query("""
//            update TaskEntity t set t.status = :status
//            where t.id = :id
//            """)
//    void setStatus(@Param("id") Integer id, @Param("status")TaskEntity.Status status);
}
