package com.example.instagram2.controller;

import com.example.instagram2.exception.ArgumentCheckUtil;
import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.service.LikesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/likes/")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;
    private final ArgumentCheckUtil argumentCheckUtil;

    @PostMapping("{username}/{imageId}")
    public ResponseEntity<?> like(@PathVariable String username,
                                  @AuthenticationPrincipal AuthMemberDTO authMember,
                                  @PathVariable Long imageId){

        argumentCheckUtil.existByUsername(username);
        argumentCheckUtil.existByImageId(imageId);
        likesService.like(imageId, authMember.getId());
        return ResponseEntity.ok().body("like");
    }

    @DeleteMapping("{username}/{imageId}")
    public ResponseEntity<?> unlike(@PathVariable String username,
                                    @AuthenticationPrincipal AuthMemberDTO authMember,
                                    @PathVariable Long imageId){

        argumentCheckUtil.existByUsername(username);
        argumentCheckUtil.existByImageId(imageId);
        likesService.undoLike(imageId, authMember.getId());
        return ResponseEntity.ok().body("unlike");
    }
}