package com.example.instagram2.controller;

import com.example.instagram2.exception.ArgumentCheckUtil;
import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.service.LikesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;
    private final ArgumentCheckUtil argumentCheckUtil;

    @PostMapping("/{username}/{imageId}/likes")
    public ResponseEntity<?> like(@PathVariable String username,
                                  @AuthenticationPrincipal AuthMemberDTO authMember,
                                  @PathVariable Long imageId){

        argumentCheckUtil.existByUsername(username);
        argumentCheckUtil.existByImageId(imageId);
        likesService.like(imageId, authMember.getId());
        return ResponseEntity.ok().body("like");
    }

    @DeleteMapping("/{username}/{imageId}/likes")
    public ResponseEntity<?> unlike(@PathVariable String username,
                                    @AuthenticationPrincipal AuthMemberDTO authMember,
                                    @PathVariable Long imageId){

        argumentCheckUtil.existByUsername(username);
        argumentCheckUtil.existByImageId(imageId);
        likesService.undoLike(imageId, authMember.getId());
        return ResponseEntity.ok().body("unlike");
    }
}
