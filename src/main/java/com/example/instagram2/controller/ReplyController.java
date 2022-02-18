package com.example.instagram2.controller;


import com.example.instagram2.dto.ReplyReqDTO;
import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.exception.ArgumentCheckUtil;
import com.example.instagram2.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Log4j2
@RequestMapping("/reply/")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;
    private final ArgumentCheckUtil argumentCheckUtil;

    @GetMapping("{username}/{imageId}")
    public ResponseEntity<?> getAllReply(@PathVariable String username,
                                          @PathVariable Long imageId,
                                          @PageableDefault(
                                                  size = 10,
                                                  sort = "regDate",
                                                  direction = Sort.Direction.DESC) Pageable pageable) {

        log.info("----------getReplyList----------");

        argumentCheckUtil.existByUsername(username);
        argumentCheckUtil.existByImageId(imageId);
        log.info("username: {}", username);
        log.info("imageId: {}", imageId);
        return ResponseEntity.ok().body(replyService.getList(imageId, pageable));
    }

    @DeleteMapping("{username}/{imageId}")
    public ResponseEntity<?> remove(@PathVariable String username,
                                    @PathVariable Long imageId,
                                    @RequestParam Long rno,
                                    @AuthenticationPrincipal AuthMemberDTO authMember) {
        log.info("----------remove----------");
        log.info(rno);

        argumentCheckUtil.existByUsername(username);
        argumentCheckUtil.existByImageId(imageId);
        replyService.remove(rno, authMember.getId());
        return ResponseEntity.ok().body("removed");

    }

    @PostMapping("{username}/{imageId}")
    public ResponseEntity<?> replyRegister(@PathVariable String username,
                                           @PathVariable Long imageId,
                                           @RequestBody @Valid ReplyReqDTO dto,
                                           @AuthenticationPrincipal AuthMemberDTO authMember) {

        argumentCheckUtil.existByUsername(username);
        argumentCheckUtil.existByImageId(imageId);
        dto.setIno(imageId);
        Long rno = replyService.register(dto, authMember);
        return ResponseEntity.ok().body(rno);
    }
}
