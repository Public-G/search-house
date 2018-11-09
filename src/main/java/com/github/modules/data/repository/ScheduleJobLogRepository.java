package com.github.modules.data.repository;

import com.github.modules.data.entity.ScheduleJobLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ScheduleJobLogRepository extends JpaRepository<ScheduleJobLogEntity, Long>, JpaSpecificationExecutor<ScheduleJobLogEntity> {
}
