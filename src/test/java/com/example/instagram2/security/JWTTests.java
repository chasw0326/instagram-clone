package com.example.instagram2.security;

import com.example.instagram2.security.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JWTTests {

    private JWTUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        System.out.println("SetUp before test.................................");
        jwtUtil = new JWTUtil();
    }

    // JWTUtil에서 만료기간 1초로 설정하고 테스트
    @Test
    public void expiryTest() throws Exception {
        String email = "example@naver.com";

        String token = jwtUtil.generateToken(email);

        Thread.sleep(3000);

        try {
            String extractedEmail = jwtUtil.validateAndExtract(token);
        }
        catch (Exception e) {
            assertThrows(ExpiredJwtException.class,()->
                    e.getClass());
        }
    }

    @Test
    public void EncodeTest() throws Exception {
        String email = "member1@google.com";
        String token = jwtUtil.generateToken(email);

        System.out.println("Below is the generated token...................");
        System.out.println(token);
    }

    @Test
    public void extractTest() throws Exception {
        String email = "example@naver.com";

        String token = jwtUtil.generateToken(email);

        String extractedEmail = jwtUtil.validateAndExtract(token);

        System.out.println(extractedEmail);

        assertEquals(email, extractedEmail);
    }
}
