package com.github.modules.search.service.impl;

import com.github.modules.search.entity.UserEntity;
import com.github.modules.search.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        UserEntity user = userService.findByMobile(mobile);

        UserEntity userEntity = null;

        // 为其注册
        if (user == null) {
            userEntity = new UserEntity(mobile, new Date(), new Date());
        } else {
            user.setLastLoginTime(new Date());
            userEntity = user;
        }

        return userService.save(userEntity);
    }
}
