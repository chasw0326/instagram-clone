package com.example.instagram2.controller;

import com.example.instagram2.dto.ResponseDTO;
import com.example.instagram2.dto.SignUpDTO;
import com.example.instagram2.entity.Member;
import com.example.instagram2.security.util.JWTUtil;
import com.example.instagram2.service.serviceImpl.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final JWTUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDTO signUpDTO) {
        try {
            log.info("-------------try to registerUser-------------");
            Member member = authService.signUp(signUpDTO);
            SignUpDTO respUserDTO = SignUpDTO.builder()
                    .id(member.getMno())
                    .email(member.getEmail())
                    .name(member.getName())
                    .username(member.getUsername())
                    .build();
            log.info("-------------success to register user-------------");
            return ResponseEntity.ok().body(respUserDTO);
        } catch (Exception e) {
            log.warn("-------------Error while registering -------------");
            ResponseDTO responseDTO = ResponseDTO.builder().
                    error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}

