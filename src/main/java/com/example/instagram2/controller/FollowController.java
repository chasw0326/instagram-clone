package com.example.instagram2.controller;


import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.exception.ArgumentCheckUtil;
import com.example.instagram2.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/follow/")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final ArgumentCheckUtil argumentCheckUtil;

    @PostMapping("{toMemberId}")
    public ResponseEntity<?> follow(@AuthenticationPrincipal AuthMemberDTO authMember,
                                    @PathVariable Long toMemberId) {

        argumentCheckUtil.existByMemberId(toMemberId);
        followService.follow(authMember.getId(), toMemberId);
        return ResponseEntity.ok().body("follow");
    }

    @DeleteMapping("{toMemberId}")
    public ResponseEntity<?> unfollow(@AuthenticationPrincipal AuthMemberDTO authMember,
                                      @PathVariable Long toMemberId){

        argumentCheckUtil.existByMemberId(toMemberId);
        followService.unFollow(authMember.getId(), toMemberId);
        return ResponseEntity.ok().body("unfollow");
    }
}
