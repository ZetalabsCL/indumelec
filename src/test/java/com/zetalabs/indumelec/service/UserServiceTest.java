package com.zetalabs.indumelec.service;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static junit.framework.TestCase.assertNotNull;

public class UserServiceTest {
    @Test
    public void Test(){
        String encoded = new BCryptPasswordEncoder().encode("admin");

        assertNotNull(encoded);
    }
}
