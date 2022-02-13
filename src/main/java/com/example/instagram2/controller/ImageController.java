package com.example.instagram2.controller;

import com.example.instagram2.dto.ImageReqDTO;
import com.example.instagram2.dto.ReplyReqDTO;
import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Reply;
import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.service.ImageService;
import com.example.instagram2.service.MemberService;
import com.example.instagram2.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/")
    public ResponseEntity<?> getFeedImages(@AuthenticationPrincipal AuthMemberDTO authMember,
                                           @PageableDefault(
                                                   size = 10,
                                                   sort = "regDate",
                                                   direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Image> images = imageService.getFeedImage(authMember.getId(), pageable);
        return ResponseEntity.ok().body(images);
    }

    @PostMapping("/create/style")
    public ResponseEntity<?> upload(@RequestBody ImageReqDTO imageReqDTO,
                                    @AuthenticationPrincipal AuthMemberDTO authMember) {
        try {
            Long ino = imageService.uploadPicture(imageReqDTO, authMember);
            return ResponseEntity.ok().body("id: " + ino);
        } catch (Exception e) {
            String error = e.getMessage();
            return ResponseEntity.badRequest().body(error);
        }
    }


    @GetMapping("/{username}/explore")
    public ResponseEntity<?> getPopularPicture(@PathVariable String username) {
        try {
            List<Image> images = imageService.getPopularImageList(username);
            return ResponseEntity.ok().body(images);
        } catch (Exception e) {
            String error = e.getMessage();
            return ResponseEntity.badRequest().body(error);
        }
    }
}

