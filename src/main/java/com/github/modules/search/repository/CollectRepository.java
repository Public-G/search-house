package com.github.modules.search.repository;

import com.github.modules.search.entity.CollectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectRepository extends JpaRepository<CollectEntity, Long> {

    CollectEntity findByUserIdAndHouseId(Long userId, String houseId);

    List<CollectEntity> findByUserId(Long userId);
}
