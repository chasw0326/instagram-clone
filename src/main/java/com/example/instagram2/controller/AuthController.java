package com.example.instagram2.controller;

import com.example.instagram2.dto.PasswordDTO;
import com.example.instagram2.dto.SignupDTO;
import com.example.instagram2.dto.UserEditDTO;
import com.example.instagram2.entity.Member;
import com.example.instagram2.exception.ArgumentCheckUtil;
import com.example.instagram2.exception.myException.InvalidPasswordException;
import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.service.MemberService;
import com.example.instagram2.util.AuthUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "회원정보 API")
@RestController
@Log4j2
@RequestMapping("/accounts/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUtil authService;
    private final MemberService memberService;
    private final ArgumentCheckUtil argumentCheckUtil;

    @ApiOperation(value = "회원가입")
    @PostMapping("signup")
    public ResponseEntity<?> registerUser(@ApiParam(value = "가입할때 보내줄 정보") @RequestBody @Valid SignupDTO signUpDTO) {
        log.info("registerUser");
        Member member = authService.signup(signUpDTO);
        SignupDTO respUserDTO = authService.entityToDTO(member);
        return ResponseEntity.ok().body(respUserDTO);
    }

    @ApiOperation(value = "비밀번호 변경화면 정보")
    @GetMapping("password/change")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal AuthMemberDTO authMember) {
        log.info("getUserInfo by {}", authMember);
        PasswordDTO dto = memberService.getProfileImgUrlAndUsernameById(authMember.getId());
        return ResponseEntity.ok().body(dto);
    }

    @ApiOperation(value = "비밀번호 변경")
    @PostMapping("password/change")
    public ResponseEntity<?> changePassword(@ApiParam(value = "비밀번호 변경값들")@RequestBody @Valid PasswordDTO passwordDTO,
                                            @AuthenticationPrincipal AuthMemberDTO authMember) throws InvalidPasswordException {
        log.info("changePw");
        passwordDTO.setMno(authMember.getId());
        authService.changePassword(passwordDTO);
        return ResponseEntity.ok().body("change");

    }

    @ApiOperation(value = "유저 정보 변경화면")
    @GetMapping("edit")
    public ResponseEntity<?> edit(@AuthenticationPrincipal AuthMemberDTO authMember) {
        UserEditDTO dto = memberService.getMemberInfo(authMember.getId());
        return ResponseEntity.ok().body(dto);
    }


    @ApiOperation(value = "유저 정보 변경")
    @PutMapping("edit")
    public ResponseEntity<?> modifyProfile(@ApiParam(value = "유저 정보 변경 값")@RequestBody @Valid UserEditDTO dto,
                                           @AuthenticationPrincipal AuthMemberDTO authMember) {

        memberService.modifyMemberInfo(authMember.getId(), dto);
        return ResponseEntity.ok().body("modify");
    }

    @ApiOperation(value = "중복로그인 방지")
    @GetMapping("login")
    public ResponseEntity<?> login(@AuthenticationPrincipal AuthMemberDTO authMember) {
        return ResponseEntity.badRequest().body("이미 로그인된 상태입니다.");
    }
}
