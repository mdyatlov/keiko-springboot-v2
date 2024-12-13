package com.theodo.albeniz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.theodo.albeniz.model.UserEntity;

public interface UserEntityRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByUsername(@Param("username") String username);
}
