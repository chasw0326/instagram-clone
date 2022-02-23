package com.example.instagram2.controller;


import com.example.instagram2.dto.FollowRespDTO;
import com.example.instagram2.dto.UserProfileRespDTO;
import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.exception.ArgumentCheckUtil;
import com.example.instagram2.service.FollowService;
import com.example.instagram2.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;


@RestController
@Log4j2
@RequestMapping("/user/")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final FollowService followService;
    private final ArgumentCheckUtil argumentCheckUtil;

    @GetMapping("{username}")
    public ResponseEntity<?> getProfile(@PathVariable String username,
                                        @AuthenticationPrincipal AuthMemberDTO authMember) {

        argumentCheckUtil.existByUsername(username);
        UserProfileRespDTO dto = memberService.getUserProfile(username, authMember.getId());
        return ResponseEntity.ok().body(dto);

    }


    @PutMapping("{username}/changePicture")
    public ResponseEntity<?> changeProfilePicture(@PathVariable String username,
                                                  @RequestParam("image") MultipartFile imageFile,
                                                  @AuthenticationPrincipal AuthMemberDTO authMember) {

        argumentCheckUtil.existByUsername(username);

        Long mno = memberService.getMemberIdByUsername(username);
        if (!mno.equals(authMember.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("no authority");
        }
        memberService.changeProfilePicture(imageFile, authMember.getId());
        return ResponseEntity.ok().body("change");
    }

    @GetMapping("{username}/followerList")
    public ResponseEntity<?> getFollowerList(@AuthenticationPrincipal AuthMemberDTO authMember,
                                             @PathVariable String username,
                                             @PageableDefault(
                                                     size = 10,
                                                     sort = "regDate",
                                                     direction = Sort.Direction.DESC) Pageable pageable) {

        argumentCheckUtil.existByUsername(username);
        List<FollowRespDTO> followerList = followService.getFollowerList(authMember.getId(), username, pageable);
        return ResponseEntity.ok().body(followerList);

    }

    @GetMapping("{username}/followList")
    public ResponseEntity<?> getFollowList(@AuthenticationPrincipal AuthMemberDTO authMember,
                                           @PathVariable String username,
                                           @PageableDefault(
                                                   size = 10,
                                                   sort = "regDate",
                                                   direction = Sort.Direction.DESC) Pageable pageable) {

        argumentCheckUtil.existByUsername(username);
        List<FollowRespDTO> followList = followService.getFollowList(authMember.getId(), username, pageable);
        return ResponseEntity.ok().body(followList);

    }

}