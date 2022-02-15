package com.example.instagram2.controller;

import com.example.instagram2.dto.PasswordDTO;
import com.example.instagram2.dto.ResponseDTO;
import com.example.instagram2.dto.SignUpDTO;
import com.example.instagram2.entity.Member;
import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.security.util.JWTUtil;
import com.example.instagram2.service.MemberService;
import com.example.instagram2.security.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
public class AuthController {

    private final AuthUtil authService;
    private final MemberService memberService;

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

    @GetMapping("/accounts/password/change")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal AuthMemberDTO authMember) {
        System.out.println("who are you" + authMember);
        try {
            Object userInfo = memberService.getProfileImgUrlAndUsernameById(authMember.getId());
            Object[] result = (Object[]) userInfo;
            List<String> dtos = new ArrayList<>();
            dtos.add((String) result[0]);
            dtos.add((String) result[1]);
            ResponseDTO<String> responseDTO = ResponseDTO.<String>builder()
                    .data(dtos)
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            String error = e.getMessage();
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/accounts/password/change")
    public ResponseEntity<?> changePassword(@RequestBody PasswordDTO passwordDTO,
                                            @AuthenticationPrincipal AuthMemberDTO authMember) {
        try {
            passwordDTO.setMno(authMember.getId());
            authService.changePassword(passwordDTO);
            return ResponseEntity.ok().body("change");
        } catch (Exception e) {
            String error = e.getMessage();
            return ResponseEntity.badRequest().body(error);
        }

    }
}

