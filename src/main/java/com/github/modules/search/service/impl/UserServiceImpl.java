package com.github.modules.search.service.impl;

import com.github.modules.search.entity.UserEntity;
import com.github.modules.search.repository.UserRepository;
import com.github.modules.search.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserEntity findByMobile(String mobile) {
        return userRepository.findByMobile(mobile);
    }

    @Transactional
    @Override
    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }
}
