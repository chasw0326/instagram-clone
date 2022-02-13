package com.example.instagram2.controller;


import com.example.instagram2.dto.ReplyReqDTO;
import com.example.instagram2.dto.ResponseDTO;
import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reply/")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping(value = "")
    public ResponseEntity<?> register(@RequestBody ReplyReqDTO replyDTO, @AuthenticationPrincipal AuthMemberDTO authMember) {
        log.info("----------register----------");
        log.info(replyDTO);
        try {
            replyDTO.setMno(authMember.getId());
            Long num = replyService.register(replyDTO, authMember);
            return ResponseEntity.ok().body(num);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error(error)
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("all")
    public ResponseEntity<?> getList(Long ino){
        log.info("----------getList----------");
        log.info(ino);
        return ResponseEntity.ok().body(replyService.getList(ino));
    }

    @DeleteMapping("{rno}")
    public ResponseEntity<?> remove(@PathVariable Long rno, @AuthenticationPrincipal AuthMemberDTO authMember) {
        log.info("----------remove----------");
        log.info(rno);
        try {
            replyService.remove(rno, authMember.getId());
            return ResponseEntity.ok().body("removed");
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error(error)
                    .build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @PostMapping("/{username}/{imageId}/reply")
    public ResponseEntity<?> replyRegister(@PathVariable String username,
                                           @PathVariable Long imageId,
                                           @RequestBody ReplyReqDTO dto,
                                           @AuthenticationPrincipal AuthMemberDTO authMember) {

        dto.setIno(imageId);
        Long rno = replyService.register(dto, authMember);
        return ResponseEntity.ok().body(rno);
    }
}
