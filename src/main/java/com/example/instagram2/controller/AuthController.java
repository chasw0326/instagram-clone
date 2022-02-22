package com.example.instagram2.controller;

import com.example.instagram2.dto.PasswordDTO;
import com.example.instagram2.dto.SignupDTO;
import com.example.instagram2.dto.UserEditDTO;
import com.example.instagram2.entity.Member;
import com.example.instagram2.exception.ArgumentCheckUtil;
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
@RequestMapping("/accounts/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUtil authService;
    private final MemberService memberService;
    private final ArgumentCheckUtil argumentCheckUtil;

    @PostMapping("signup")
    public ResponseEntity<?> registerUser(@RequestBody @Valid SignupDTO signUpDTO) {
        log.info("registerUser");
        Member member = authService.signup(signUpDTO);
        SignupDTO respUserDTO = authService.entityToDTO(member);
        return ResponseEntity.ok().body(respUserDTO);
    }

    @GetMapping("password/change")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal AuthMemberDTO authMember) {
        log.info("getUserInfo by {}", authMember);
        PasswordDTO dto = memberService.getProfileImgUrlAndUsernameById(authMember.getId());
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("password/change")
    public ResponseEntity<?> changePassword(@RequestBody @Valid PasswordDTO passwordDTO,
                                            @AuthenticationPrincipal AuthMemberDTO authMember) {
        log.info("changePw");
        passwordDTO.setMno(authMember.getId());
        authService.changePassword(passwordDTO);
        return ResponseEntity.ok().body("change");

    }

    @GetMapping("edit")
    public ResponseEntity<?> edit(@AuthenticationPrincipal AuthMemberDTO authMember) {
        UserEditDTO dto = memberService.getMemberInfo(authMember.getId());
        return ResponseEntity.ok().body(dto);
    }


    @PutMapping("edit")
    public ResponseEntity<?> modifyProfile(@RequestBody @Valid UserEditDTO dto,
                                           @AuthenticationPrincipal AuthMemberDTO authMember) {

        memberService.modifyMemberInfo(authMember.getId(), dto);
        return ResponseEntity.ok().body("modify");
    }

    @GetMapping("login")
    public ResponseEntity<?> login(@AuthenticationPrincipal AuthMemberDTO authMember) {
        return ResponseEntity.badRequest().body("이미 로그인된 상태입니다.");
    }
}
