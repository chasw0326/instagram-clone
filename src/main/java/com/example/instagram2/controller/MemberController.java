package com.example.instagram2.controller;


import com.example.instagram2.dto.FollowRespDTO;
import com.example.instagram2.dto.UserProfileRespDTO;
import com.example.instagram2.exception.myException.NoAuthorityException;
import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.exception.ArgumentCheckUtil;
import com.example.instagram2.service.FollowService;
import com.example.instagram2.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

/**
 * <code>AuthController</code><br>
 * 회원가입, 비밀번호 변경등 중요한 정보들 처리
 * @author chasw326
 */
@Api(tags = "유저 프로필, 수정 API")
@RestController
@Log4j2
@RequestMapping("/user/")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final FollowService followService;
    private final ArgumentCheckUtil argumentCheckUtil;


    @ApiOperation(value = "프로필 가져오기")
    @GetMapping("{username}")
    public ResponseEntity<?> getProfile(@PathVariable String username,
                                        @AuthenticationPrincipal AuthMemberDTO authMember) {

        argumentCheckUtil.existByUsername(username);
        UserProfileRespDTO dto = memberService.getUserProfile(username, authMember.getId());
        return ResponseEntity.ok().body(dto);

    }


    @ApiOperation(value = "프로필 사진 변경")
    @PostMapping(value = "/changePicture/{username}",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> changeProfilePicture(@PathVariable String username,
                                                  @RequestPart MultipartFile imgFile,
                                                  @AuthenticationPrincipal AuthMemberDTO authMember) throws NoAuthorityException, IOException {

        argumentCheckUtil.existByUsername(username);

        Long mno = memberService.getMemberIdByUsername(username);
        if (!mno.equals(authMember.getId())) {
            throw new NoAuthorityException("no authority to change profile-picture");
        }
        memberService.changeProfilePicture(imgFile, authMember.getId());
        return ResponseEntity.ok().body("change");
    }


    @ApiOperation(value = "프로필 사진 삭제")
    @GetMapping("/deletePicture/{username}")
    public ResponseEntity<?> delteProfilePicture(@ApiParam(value = "사용자이름")@PathVariable String username,
                                                 @AuthenticationPrincipal AuthMemberDTO authMember) throws NoAuthorityException {

        argumentCheckUtil.existByUsername(username);
        Long mno = memberService.getMemberIdByUsername(username);
        if (!mno.equals(authMember.getId())) {
            throw new NoAuthorityException("no authority to delete profile-picture");
        }
        memberService.deleteProfilePicture(authMember.getId());
        return ResponseEntity.ok().body("change");

    }


    @ApiOperation(value = "팔로워 리스트 가져오기")
    @GetMapping("{username}/followerList")
    public ResponseEntity<?> getFollowerList(@AuthenticationPrincipal AuthMemberDTO authMember,
                                             @ApiParam(value = "사용자이름")@PathVariable String username,
                                             @PageableDefault(
                                                     size = 10,
                                                     sort = "regDate",
                                                     direction = Sort.Direction.DESC) Pageable pageable) {

        argumentCheckUtil.existByUsername(username);
        List<FollowRespDTO> followerList = followService.getFollowerList(authMember.getId(), username, pageable);
        return ResponseEntity.ok().body(followerList);

    }

    @ApiOperation(value = "팔로우 리스트 가져오기")
    @GetMapping("{username}/followList")
    public ResponseEntity<?> getFollowList(@AuthenticationPrincipal AuthMemberDTO authMember,
                                           @ApiParam(value = "사용자 이름")@PathVariable String username,
                                           @PageableDefault(
                                                   size = 10,
                                                   sort = "regDate",
                                                   direction = Sort.Direction.DESC) Pageable pageable) {

        argumentCheckUtil.existByUsername(username);
        List<FollowRespDTO> followList = followService.getFollowList(authMember.getId(), username, pageable);
        return ResponseEntity.ok().body(followList);
    }



}