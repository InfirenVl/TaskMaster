package com.infiren.taskmaster.repository;

import com.infiren.taskmaster.object.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
}
