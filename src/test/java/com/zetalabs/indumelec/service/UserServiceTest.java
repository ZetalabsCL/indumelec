package com.zetalabs.indumelec.service;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserServiceTest {
    @Test
    public void Test(){
        String encoded = new BCryptPasswordEncoder().encode("admin");
        System.out.println(encoded);
    }
}
