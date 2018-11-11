package com.github.modules.data.repository;

import com.github.modules.data.entity.ScheduleJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface ScheduleJobRepository extends JpaRepository<ScheduleJobEntity, Long>, JpaSpecificationExecutor<ScheduleJobEntity> {

    void deleteByJobIdIn(Collection<Long> userIds);

    @Modifying
    @Query("UPDATE ScheduleJobEntity scheduleJob SET scheduleJob.status = :statusValue WHERE scheduleJob.jobId = :jobId")
    void updateStatus(@Param("jobId") Long jobId, @Param("statusValue")int status);

}
