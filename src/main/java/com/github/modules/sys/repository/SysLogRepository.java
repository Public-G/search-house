package com.github.modules.sys.repository;

import com.github.modules.sys.entity.SysLogEntity;
import com.github.modules.sys.entity.SysUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SysLogRepository extends JpaRepository<SysLogEntity, Long>, JpaSpecificationExecutor<SysLogEntity> {
}
