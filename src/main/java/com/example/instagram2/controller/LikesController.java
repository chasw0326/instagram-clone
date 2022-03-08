package com.example.instagram2.controller;

import com.example.instagram2.exception.ArgumentCheckUtil;
import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.service.LikesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = "좋아요 API")
@RestController
@Log4j2
@RequestMapping("/likes/")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;
    private final ArgumentCheckUtil argumentCheckUtil;


    @ApiOperation(value = "좋아요")
    @PostMapping("{imageId}")
    public ResponseEntity<?> like(@AuthenticationPrincipal AuthMemberDTO authMember,
                                  @ApiParam(value = "좋아요할 사진 id")@PathVariable Long imageId) {

        argumentCheckUtil.existByImageId(imageId);
        likesService.like(imageId, authMember.getId());
        return ResponseEntity.ok().body("like");
    }

    @ApiOperation(value = "좋아요 취소")
    @DeleteMapping("{imageId}")
    public ResponseEntity<?> unlike(@AuthenticationPrincipal AuthMemberDTO authMember,
                                    @ApiParam(value = "좋아요 취소할 사진 id")@PathVariable Long imageId) {

        argumentCheckUtil.existByImageId(imageId);
        likesService.undoLike(imageId, authMember.getId());
        return ResponseEntity.ok().body("unlike");
    }
}
