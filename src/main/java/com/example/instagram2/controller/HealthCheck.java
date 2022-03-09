package com.example.instagram2.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <code>HealthCheck</code><br>
 * 배포용 헬스체크 입니다.
 * @author chasw326
 */
@Api(tags = "헬스체크")
@RestController
public class HealthCheck {

    @GetMapping("/")
    public String healthCheck() {
        return "The Service is up and running...";
    }
}
