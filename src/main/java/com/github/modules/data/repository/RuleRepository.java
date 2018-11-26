package com.github.modules.data.repository;

import com.github.modules.data.entity.RuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface RuleRepository extends JpaRepository<RuleEntity, Long>, JpaSpecificationExecutor<RuleEntity> {

    void deleteByRuleIdIn(Long[] ruleIds);

    RuleEntity findByRuleName(String ruleName);
}
