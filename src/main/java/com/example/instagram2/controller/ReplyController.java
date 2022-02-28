package com.example.instagram2.controller;


import com.example.instagram2.dto.ReplyReqDTO;
import com.example.instagram2.exception.myException.NoAuthorityException;
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
import java.util.List;

@RestController
@Log4j2
@RequestMapping("/reply/")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;
    private final ArgumentCheckUtil argumentCheckUtil;

    @GetMapping("{imageId}")
    public ResponseEntity<?> getAllReply(@PathVariable Long imageId,
                                         @PageableDefault(
                                                 size = 10,
                                                 sort = "regDate",
                                                 direction = Sort.Direction.ASC) Pageable pageable) {

        log.info("----------getReplyList----------");

        argumentCheckUtil.existByImageId(imageId);
        log.info("imageId: {}", imageId);
        List<ReplyReqDTO> dto = replyService.getList(imageId, pageable);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("{imageId}/{replyId}")
    public ResponseEntity<?> remove(@PathVariable Long imageId,
                                    @PathVariable Long replyId,
                                    @AuthenticationPrincipal AuthMemberDTO authMember) throws NoAuthorityException {
        log.info("----------remove----------");
        log.info(replyId);

        argumentCheckUtil.existByImageId(imageId);
        replyService.remove(replyId, authMember.getId());
        return ResponseEntity.ok().body("removed");

    }

    @PostMapping("{imageId}")
    public ResponseEntity<?> replyRegister(@PathVariable Long imageId,
                                           @RequestBody @Valid ReplyReqDTO dto,
                                           @AuthenticationPrincipal AuthMemberDTO authMember) {

        argumentCheckUtil.existByImageId(imageId);
        dto.setIno(imageId);
        Long rno = replyService.register(dto, authMember);
        return ResponseEntity.ok().body(rno);
    }
}
