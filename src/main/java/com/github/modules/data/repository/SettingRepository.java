package com.github.modules.data.repository;

import com.github.modules.data.entity.SettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SettingRepository extends JpaRepository<SettingEntity, Long>, JpaSpecificationExecutor<SettingEntity> {

    SettingEntity findBySettingName(String settingName);

    void deleteBySettingIdIn(Long[] settingIds);
}
