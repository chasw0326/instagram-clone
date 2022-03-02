package com.example.instagram2.serviceTests;

import com.example.instagram2.security.util.PasswordStrength;
import com.example.instagram2.security.util.PasswordUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PasswordUtilTests {

    PasswordUtil passwordUtil = new PasswordUtil();

    @Test
    void verifyMeter() {
        assertEquals(PasswordStrength.STRONG, passwordUtil.meter("abcABC123!@#"));
    }
}