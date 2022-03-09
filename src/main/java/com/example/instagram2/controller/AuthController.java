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

/**
 * <code>AuthController</code><br>
 * 회원가입, 비밀번호 변경등 중요한 정보들 처리
 * @author chasw326
 */
@Api(tags = "회원정보 API")
@RestController
@Log4j2
@RequestMapping("/accounts/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUtil authService;
    private final MemberService memberService;
    private final ArgumentCheckUtil argumentCheckUtil;

    /**
     * 이메일, 사용자이름, 비밀번호를 보내주세요. <br>
     * valid로 유효한지 체크합니다.
     * @param signUpDTO
     * @return
     */
    @ApiOperation(value = "회원가입")
    @PostMapping("signup")
    public ResponseEntity<?> registerUser(@ApiParam(value = "가입할때 보내줄 정보") @RequestBody @Valid SignupDTO signUpDTO) {
        log.info("registerUser");
        Member member = authService.signup(signUpDTO);
        SignupDTO respUserDTO = authService.entityToDTO(member);
        return ResponseEntity.ok().body(respUserDTO);
    }

    /**
     * 비밀번호 변경화면에 필요한 정보들을 보내줍니다.
     * @param authMember
     * @return
     */
    @ApiOperation(value = "비밀번호 변경화면 정보")
    @GetMapping("password/change")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal AuthMemberDTO authMember) {
        log.info("getUserInfo by {}", authMember);
        PasswordDTO dto = memberService.getProfileImgUrlAndUsernameById(authMember.getId());
        return ResponseEntity.ok().body(dto);
    }

    /**
     * 현재비밀번호, 바꿀비밀번호, 체크비밀번호를 보내주세요.
     * 비밀번호 강도를 체크합니다.
     * @param passwordDTO
     * @param authMember
     * @return
     * @throws InvalidPasswordException
     */
    @ApiOperation(value = "비밀번호 변경")
    @PostMapping("password/change")
    public ResponseEntity<?> changePassword(@ApiParam(value = "비밀번호 변경값들")@RequestBody @Valid PasswordDTO passwordDTO,
                                            @AuthenticationPrincipal AuthMemberDTO authMember) throws InvalidPasswordException {
        log.info("changePw");
        passwordDTO.setMno(authMember.getId());
        authService.changePassword(passwordDTO);
        return ResponseEntity.ok().body("change");

    }

    /**
     * 유저정보 변경화면에 필요한 값들을 보내줍니다.<br>
     * <strong>중요! 받은 값들 중에 email은 ReadOnly로 해주세요.</strong>
     * @param authMember
     * @return
     */
    @ApiOperation(value = "유저 정보 변경화면")
    @GetMapping("edit")
    public ResponseEntity<?> edit(@AuthenticationPrincipal AuthMemberDTO authMember) {
        UserEditDTO dto = memberService.getMemberInfo(authMember.getId());
        return ResponseEntity.ok().body(dto);
    }

    /**
     * UserEditDTO를 참조해서 유저 정보 변경값들을 보내주세요.
     * @param dto
     * @param authMember
     * @return
     * @see com.example.instagram2.dto.UserEditDTO
     *
     */
    @ApiOperation(value = "유저 정보 변경")
    @PutMapping("edit")
    public ResponseEntity<?> modifyProfile(@ApiParam(value = "유저 정보 변경 값")@RequestBody @Valid UserEditDTO dto,
                                           @AuthenticationPrincipal AuthMemberDTO authMember) {

        memberService.modifyMemberInfo(authMember.getId(), dto);
        return ResponseEntity.ok().body("modify");
    }

    /**
     * 중복로그인을 방지합니다.
     * @param authMember
     * @return
     */
    @ApiOperation(value = "중복로그인 방지")
    @GetMapping("login")
    public ResponseEntity<?> login(@AuthenticationPrincipal AuthMemberDTO authMember) {
        return ResponseEntity.badRequest().body("이미 로그인된 상태입니다.");
    }
}
