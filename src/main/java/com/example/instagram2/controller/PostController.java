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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
public class PostController {

    private final ImageService imageService;
    private final ReplyService replyService;
    private final MemberService memberService;


    @GetMapping("/")
    public ResponseEntity<?> getFeedImages(@AuthenticationPrincipal AuthMemberDTO authMember,
                                           @PageableDefault(
                                                   size = 5,
                                                   sort = "regDate",
                                                   direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Image> images = imageService.getFeedImage(authMember.getId(), pageable);
        return ResponseEntity.ok().body(images);
    }

    @PostMapping("/{username}/upload")
    public ResponseEntity<?> upload(@PathVariable String username,
                                    @RequestBody ImageReqDTO imageReqDTO,
                                    @AuthenticationPrincipal AuthMemberDTO authMember) {
        try {
            Long mno = memberService.getMemberIdByUsername(username);
            if (mno.equals(authMember.getId())) {
                Long ino = imageService.uploadPicture(imageReqDTO, authMember);
                return ResponseEntity.ok().body("id: " + ino);
            }
        }catch (Exception e){
            String error = e.getMessage();
            return ResponseEntity.badRequest().body(error);
        }
        return ResponseEntity.badRequest().body("wrong id");
    }

    @GetMapping("/{username}/explore")
    public ResponseEntity<?> getPopularPicture(@PathVariable String username,
                                               @AuthenticationPrincipal AuthMemberDTO authMember) {

        Long mno = memberService.getMemberIdByUsername(username);
        if (mno.equals(authMember.getId())) {
            List<Image> images = imageService.getPopularImageList(authMember.getId());
            return ResponseEntity.ok().body(images);
        } else {
            return ResponseEntity.badRequest().body("wrong id");
        }

    }

    @PostMapping("/{username}/{imageId}/comment")
    public ResponseEntity<?> replyRegister(@PathVariable String username,
                                           @PathVariable Long imageId,
                                           @RequestBody ReplyReqDTO dto,
                                           @AuthenticationPrincipal AuthMemberDTO authMember) {

        if (!authMember.isEnabled()) {
            return ResponseEntity.ok().body("wrong id");
        }
        Long rno = replyService.register(dto, authMember);
        dto.setRno(rno);
        return ResponseEntity.ok().body(dto);
    }


}

