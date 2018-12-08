package com.github.modules.search.repository;

import com.github.modules.search.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByMobile(String mobile);
}
