package com.github.modules.data.repository;

import com.github.modules.data.entity.SpiderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SpiderRepository extends JpaRepository<SpiderEntity, Long>, JpaSpecificationExecutor<SpiderEntity> {

    void deleteBySpiderIdIn(Long[] spiderIds);
}
