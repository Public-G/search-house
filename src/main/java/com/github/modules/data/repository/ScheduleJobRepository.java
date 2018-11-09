package com.github.modules.data.repository;

import com.github.modules.data.entity.ScheduleJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ScheduleJobRepository extends JpaRepository<ScheduleJobEntity, Long>, JpaSpecificationExecutor<ScheduleJobEntity> {

    ScheduleJobEntity findByJobId(Long jobId);

}
