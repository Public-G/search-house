package com.github.modules.sys.repository;

import com.github.modules.sys.entity.SysRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SysRoleRepository extends JpaRepository<SysRoleEntity, Long>, JpaSpecificationExecutor<SysRoleEntity> {

    SysRoleEntity findByRoleName(String roleName);

    void deleteByRoleIdIn(Long[] roleIds);
}
