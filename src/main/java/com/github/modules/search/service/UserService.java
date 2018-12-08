package com.github.modules.search.service;

import com.github.modules.search.entity.UserEntity;

/**
 * 普通用户
 *
 * @author ZEALER
 * @date 2018-12-06
 */
public interface UserService {

    UserEntity findByMobile(String mobile);

    UserEntity save(UserEntity userEntity);
}
