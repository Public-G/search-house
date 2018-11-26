package com.github.modules.data.repository;

import com.github.modules.data.entity.SpiderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SpiderRepository extends JpaRepository<SpiderEntity, Long>, JpaSpecificationExecutor<SpiderEntity> {

    void deleteBySpiderIdIn(Long[] spiderIds);

    SpiderEntity findBySpiderName(String spiderName);

    @Modifying
    @Query("UPDATE SpiderEntity spider SET spider.ruleId = null WHERE spider.ruleId in ?1")
    void deleteRuleBatch(Long[] ruleIds);

    @Modifying
    @Query("UPDATE SpiderEntity spider SET spider.settingId = null WHERE spider.settingId in ?1")
    void deleteSettingBatch(Long[] settingIds);
}
