package com.example.instagram2.controller;


import com.example.instagram2.dto.FollowRespDTO;
import com.example.instagram2.dto.UserProfileRespDTO;
import com.example.instagram2.dto.UserUpdateReqDTO;
import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.service.FollowService;
import com.example.instagram2.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


@RestController
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final FollowService followService;


    @GetMapping("/{pageUserId}/followerList")
    public ResponseEntity<?> getFollowerList(@AuthenticationPrincipal AuthMemberDTO authMember,
                                             @PathVariable Long pageUserId,
                                             @PageableDefault(
                                                     size = 10,
                                                     sort = "regDate",
                                                     direction = Sort.Direction.DESC) Pageable pageable) {

        try {
            Page<FollowRespDTO> followerList = followService.getFollowerList(authMember.getId(), pageUserId, pageable);

            return ResponseEntity.ok().body(followerList);
        } catch (Exception e) {
            String error = e.getMessage();
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{pageUserId}/followList")
    public ResponseEntity<?> getFollowList(@AuthenticationPrincipal AuthMemberDTO authMember,
                                           @PathVariable Long pageUserId,
                                           @PageableDefault(
                                                   size = 10,
                                                   sort = "regDate",
                                                   direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            Page<FollowRespDTO> followList = followService.getFollowList(authMember.getId(), pageUserId, pageable);

            return ResponseEntity.ok().body(followList);
        } catch (Exception e) {
            String error = e.getMessage();
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getProfile(@PathVariable String username,
                                        @AuthenticationPrincipal AuthMemberDTO authMember) throws URISyntaxException {
        try {
            if (authMember.isEnabled()) {
                UserProfileRespDTO dto = memberService.getUserProfile(username, authMember.getId());
                return ResponseEntity.ok().body(dto);
            } else {
                URI redirectURI = new URI("http://localhost:3000/login");
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setLocation(redirectURI);
                return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
            }
        } catch (Exception e) {
            log.warn("error getProfile");
            String error = e.getMessage();
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/accounts/edit")
    public ResponseEntity<?> edit(@AuthenticationPrincipal AuthMemberDTO authMember) {
        try {
            UserUpdateReqDTO dto = memberService.getMemberInfo(authMember.getId());
            return ResponseEntity.ok().body(dto);
        } catch (Exception e) {
            String error = e.getMessage();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("wrong user");
        }
    }


    @PutMapping("/accounts/edit")
    public ResponseEntity<?> modifyProfile(@RequestBody UserUpdateReqDTO dto,
                                           @AuthenticationPrincipal AuthMemberDTO authMember) {
        try {
            memberService.modifyMemberInfo(authMember.getId(), dto);
            return ResponseEntity.ok().body("modify");
        } catch (Exception e) {
            String error = e.getMessage();
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{username}/changePicture")
    public ResponseEntity<?> changeProfilePicture(@PathVariable String username,
                                                  MultipartFile imageFile,
                                                  @AuthenticationPrincipal AuthMemberDTO authMember) {
        try {
            Long mno = memberService.getMemberIdByUsername(username);
            if (mno.equals(authMember.getId())) {
                memberService.changeProfilePicture(imageFile, authMember.getId());
                return ResponseEntity.ok().body("change");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("no authority");
            }
        } catch (Exception e) {
            String error = e.getMessage();
            return ResponseEntity.badRequest().body(error);
        }
    }
}