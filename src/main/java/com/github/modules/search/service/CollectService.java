package com.github.modules.search.service;

import com.github.modules.search.entity.CollectEntity;

import java.util.List;

/**
 * 房源收藏
 *
 * @author ZEALER
 * @date 2018-12-06
 */
public interface CollectService {

    /**
     * 房源收藏数
     * @param userId
     * @return
     */
    int findCount(Long userId);

    /**
     * 查找用户已收藏房源列表
     * @param userId
     * @return
     */
    List<CollectEntity> findByUserId(Long userId);

    /**
     * 查找用户收藏的指定房源
     * @param userId
     * @param houseId
     * @return
     */
    CollectEntity findByUserIdAndhouseId(Long userId, String houseId);

    /**
     * 房源收藏
     * @param userId
     * @param houseId
     */
    void save(Long userId, String houseId);

    /**
     * 删除收藏
     * @param userId
     * @param houseId
     */
    void delete(Long userId, String houseId);
}
