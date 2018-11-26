package com.github.modules.data.repository;

import com.github.modules.data.entity.SupportAreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SupportAreaRepository extends JpaRepository<SupportAreaEntity, Long>, JpaSpecificationExecutor<SupportAreaEntity> {

    SupportAreaEntity findByCnName(String cnName);

    List<SupportAreaEntity> findByParentId(Long parentId);

    List<SupportAreaEntity> findByLevel(int level);
}
