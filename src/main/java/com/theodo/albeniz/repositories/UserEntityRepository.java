package com.theodo.albeniz.repositories;

import com.theodo.albeniz.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface UserEntityRepository extends JpaRepository<UserEntity, String>, QueryByExampleExecutor<UserEntity> {
    UserEntity findByUsername(@Param("username") String username);
}
