package com.zetalabs.indumelec.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class UserServiceTest {
    @Test
    public void Test(){
        String encoded = new BCryptPasswordEncoder().encode("admin");

        assertNotNull(encoded);
    }
}
