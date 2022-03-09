package com.example.instagram2.controller;


import com.example.instagram2.dto.ReplyReqDTO;
import com.example.instagram2.exception.myException.NoAuthorityException;
import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.exception.ArgumentCheckUtil;
import com.example.instagram2.service.ReplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

/**
 * <code>AuthController</code><br>
 * 회원가입, 비밀번호 변경등 중요한 정보들 처리
 * @author chasw326
 */
@Api(tags = "댓글 API")
@RestController
@Log4j2
@RequestMapping("/reply/")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;
    private final ArgumentCheckUtil argumentCheckUtil;


    /**
     * 댓글 더보기 누르면 모든 댓글 가져옴
     * @param imageId
     * @param pageable
     * @return
     */
    @ApiOperation(value = "모든 댓글 가져오기")
    @GetMapping("{imageId}")
    public ResponseEntity<?> getAllReply(@ApiParam(value = "이미지 id")@PathVariable Long imageId,
                                         @PageableDefault(
                                                 size = 10,
                                                 sort = "regDate",
                                                 direction = Sort.Direction.ASC) Pageable pageable) {

        log.info("----------getReplyList----------");

        argumentCheckUtil.existByImageId(imageId);
        log.info("imageId: {}", imageId);
        List<ReplyReqDTO> dto = replyService.getReplyList(imageId, pageable);
        return ResponseEntity.ok().body(dto);
    }

    @ApiOperation(value = "댓글 삭제")
    @DeleteMapping("{imageId}/{replyId}")
    public ResponseEntity<?> remove(@ApiParam(value = "이미지 id")@PathVariable Long imageId,
                                    @ApiParam(value = "댓글 id")@PathVariable Long replyId,
                                    @AuthenticationPrincipal AuthMemberDTO authMember) throws NoAuthorityException {
        log.info("----------remove----------");
        log.info(replyId);

        argumentCheckUtil.existByImageId(imageId);
        replyService.remove(replyId, authMember.getId());
        return ResponseEntity.ok().body("removed");

    }

    @ApiOperation(value = "댓글 등록")
    @PostMapping("{imageId}")
    public ResponseEntity<?> replyRegister(@ApiParam(value = "이미지 id")@PathVariable Long imageId,
                                           @ApiParam(value = "댓글 dto")@RequestBody @Valid ReplyReqDTO dto,
                                           @AuthenticationPrincipal AuthMemberDTO authMember) {

        argumentCheckUtil.existByImageId(imageId);
        dto.setIno(imageId);
        Long rno = replyService.register(dto, authMember);
        return ResponseEntity.ok().body(rno);
    }
}
