package com.github.modules.search.service.impl;

import com.github.modules.search.entity.CollectEntity;
import com.github.modules.search.repository.CollectRepository;
import com.github.modules.search.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("collectService")
public class CollectServiceImpl implements CollectService {

    @Autowired
    private CollectRepository collectRepository;

    @Override
    public int findCount(Long userId) {
        return findHouseId(userId).size();
    }

    @Override
    public List<CollectEntity> findByUserId(Long userId) {
        return findHouseId(userId);

        // TODO ES批量查询
    }

    @Override
    public CollectEntity findByUserIdAndhouseId(Long userId, String houseId) {
        return collectRepository.findByUserIdAndHouseId(userId, houseId);
    }

    @Transactional
    @Override
    public void save(Long userId, String houseId) {
        CollectEntity collectEntity = new CollectEntity(userId, houseId);
        collectRepository.save(collectEntity);
    }

    @Transactional
    @Override
    public void delete(Long userId, String houseId) {
        CollectEntity collectEntity = collectRepository.findByUserIdAndHouseId(userId, houseId);
        if (collectEntity != null) {
            collectRepository.delete(collectEntity);
        }
    }

    private List<CollectEntity> findHouseId(Long userId) {
        return collectRepository.findByUserId(userId);
    }
}
