package com.example.instagram2.controller;

import com.example.instagram2.dto.PasswordDTO;
import com.example.instagram2.dto.SignUpDTO;
import com.example.instagram2.dto.UserEditDTO;
import com.example.instagram2.entity.Member;
import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.service.MemberService;
import com.example.instagram2.service.serviceImpl.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Log4j2
@RequiredArgsConstructor
public class AuthController {

    private final AuthUtil authService;
    private final MemberService memberService;

    @PostMapping("/accounts/signup")
    public ResponseEntity<?> registerUser(@RequestBody @Valid SignUpDTO signUpDTO) {
        log.info("registerUser");
        Member member = authService.signUp(signUpDTO);
        SignUpDTO respUserDTO = authService.entityToDTO(member);
        return ResponseEntity.ok().body(respUserDTO);
    }

    @GetMapping("/accounts/password/change")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal AuthMemberDTO authMember) {
        log.info("getUserInfo");
        PasswordDTO dto = memberService.getProfileImgUrlAndUsernameById(authMember.getId());
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/accounts/password/change")
    public ResponseEntity<?> changePassword(@RequestBody @Valid PasswordDTO passwordDTO,
                                            @AuthenticationPrincipal AuthMemberDTO authMember) {
        log.info("changePw");
        passwordDTO.setMno(authMember.getId());
        authService.changePassword(passwordDTO);
        return ResponseEntity.ok().body("change");

    }

    @GetMapping("/accounts/edit")
    public ResponseEntity<?> edit(@AuthenticationPrincipal AuthMemberDTO authMember) {
        UserEditDTO dto = memberService.getMemberInfo(authMember.getId());
        return ResponseEntity.ok().body(dto);
    }


    @PutMapping("/accounts/edit")
    public ResponseEntity<?> modifyProfile(@RequestBody @Valid UserEditDTO dto,
                                           @AuthenticationPrincipal AuthMemberDTO authMember) {
        memberService.modifyMemberInfo(authMember.getId(), dto);
        return ResponseEntity.ok().body("modify");
    }

    @GetMapping("/accounts/login")
    public ResponseEntity<?> login(@AuthenticationPrincipal AuthMemberDTO authMember) {
        return ResponseEntity.badRequest().body("이미 로그인된 상태입니다.");
    }
}
