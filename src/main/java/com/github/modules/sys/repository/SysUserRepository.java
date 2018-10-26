package com.github.modules.sys.repository;

import com.github.modules.sys.entity.SysUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysUserRepository extends JpaRepository<SysUserEntity, Long> {

    SysUserEntity findByUsername(String username);

}
