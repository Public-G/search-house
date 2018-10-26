package com.github.util;

import com.github.SearchHouseApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;


public class PasswordEncoderTests extends SearchHouseApplicationTests {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testPasswordEncoder() {
        String pass     = "123456";
        String hashPass = passwordEncoder.encode(pass);
        System.out.println(hashPass + "长度" + hashPass.length());

        boolean f = passwordEncoder.matches("123456", hashPass);
        System.out.println(f);
    }

}
