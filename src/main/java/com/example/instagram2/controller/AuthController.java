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

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
public class AuthController {

    private final AuthUtil authService;
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody @Valid SignUpDTO signUpDTO) {

        Member member = authService.signUp(signUpDTO);
        SignUpDTO respUserDTO = signUpDTO.entityToDTO(member);
        return ResponseEntity.ok().body(respUserDTO);
    }

    @GetMapping("/accounts/password/change")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal AuthMemberDTO authMember) {
        log.info(authMember.getUsername() + " using getUserInfo");
        PasswordDTO dto = memberService.getProfileImgUrlAndUsernameById(authMember.getId());
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/accounts/password/change")
    public ResponseEntity<?> changePassword(@RequestBody @Valid PasswordDTO passwordDTO,
                                            @AuthenticationPrincipal AuthMemberDTO authMember) {
        log.info(authMember.getUsername() + " using changePassword");

        passwordDTO.setMno(authMember.getId());
        authService.changePassword(passwordDTO);
        return ResponseEntity.ok().body("change");

    }

    @GetMapping("/accounts/login")
    public ResponseEntity<?> login(@AuthenticationPrincipal AuthMemberDTO authMember) {
            return ResponseEntity.badRequest().body("이미 로그인된 상태입니다.");
    }
}
