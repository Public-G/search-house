package com.github.modules.data.repository;

import com.github.modules.data.entity.SpiderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpiderRepository extends JpaRepository<SpiderEntity, Long>, JpaSpecificationExecutor<SpiderEntity> {

    void deleteBySpiderIdIn(Long[] spiderIds);

    SpiderEntity findBySpiderName(String spiderName);

    List<SpiderEntity> findByRuleIdIn(Long[] ruleIds);

    List<SpiderEntity> findBySettingIdIn(Long[] settingIds);

    @Query(value = "SELECT DISTINCT city FROM tb_spider", nativeQuery = true)
    List<String> findCity();

//    @Modifying
//    @Query("UPDATE SpiderEntity spider SET spider.ruleId = null WHERE spider.ruleId in ?1")
//    void deleteRuleBatch(Long[] ruleIds);
//
//    @Modifying
//    @Query("UPDATE SpiderEntity spider SET spider.settingId = null WHERE spider.settingId in ?1")
//    void deleteSettingBatch(Long[] settingIds);
}
