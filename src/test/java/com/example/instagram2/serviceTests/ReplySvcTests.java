package com.example.instagram2.serviceTests;

import com.example.instagram2.dto.ImgReply;
import com.example.instagram2.dto.ReplyReqDTO;
import com.example.instagram2.entity.Reply;
import com.example.instagram2.exception.myException.NoAuthorityException;
import com.example.instagram2.repository.ReplyRepository;
import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.service.ReplyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReplySvcTests {

    @Autowired
    private ReplyService replyService;

    @Autowired
    private ReplyRepository replyRepository;

    @DisplayName("댓글 작성, register")
    @Test
    @WithUserDetails(value = "chasw@naver.com")
    void Should_RegisterReply_WhenNormalSituation() {
        Long ino = 99L;
        AuthMemberDTO loggedUser = (AuthMemberDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ReplyReqDTO replyReqDTO = ReplyReqDTO.builder()
                .text("아무내용 댓글")
                .ino(ino)
                .build();

        Long rno = replyService.register(replyReqDTO, loggedUser);
        Optional<Reply> result = replyRepository.findById(rno);
        if (result.isPresent()) {
            Reply reply = result.get();
            assertEquals("아무내용 댓글", reply.getContent());
            assertEquals(loggedUser.getId(), reply.getMember().getMno());
            assertEquals(ino, reply.getImage().getIno());
            assertEquals(rno, reply.getRno());
        }
    }


    @DisplayName("다른사람이 댓글 삭제 시도, remove")
    @Test
    void Should_ThrowException_WhenDeleteByStranger(){
        Long rno = 43L;
        Long userId = 121L;
        Throwable ex = assertThrows(NoAuthorityException.class, () -> {
            replyService.remove(rno, userId);
        });
    }

    @DisplayName("댓글 삭제, remove")
    @Test
    void Should_Delete_WhenMyReply() throws NoAuthorityException {
        Long rno = 81L;
        Long userId = 121L;
        replyService.remove(rno, userId);
        Optional<Reply> result = replyRepository.findById(rno);
        assertFalse(result.isPresent());
    }

    @DisplayName("댓글리스트 가져오기")
    @Test
    void Should_GetReplyList(){
        PageRequest pageRequest = PageRequest.of(0, 10);

        List<ReplyReqDTO> reqDTOS = replyService.getReplyList(8L, pageRequest);
        for(ReplyReqDTO dto : reqDTOS){
            System.out.println(dto);
        }
        assertEquals(6L, reqDTOS.get(0).getRno());
        assertEquals(93L, reqDTOS.get(1).getRno());
        assertEquals(118L, reqDTOS.get(2).getRno());
        assertEquals(258L, reqDTOS.get(3).getRno());
        assertEquals(313L, reqDTOS.get(4).getRno());
        assertEquals(328L, reqDTOS.get(5).getRno());
        assertEquals(403L, reqDTOS.get(6).getRno());
    }

    @DisplayName("댓글 3개 가져오기")
    @Test
    void Should_Get3Replies(){
        List<ImgReply> imgReplies = replyService.get3Replies(4L);
        for(ImgReply r : imgReplies){
            System.out.println(r);
        }
        assertEquals(87L, imgReplies.get(0).getReply().getRno());
        assertEquals(32L, imgReplies.get(0).getUserId());
        assertEquals(137L, imgReplies.get(1).getReply().getRno());
        assertEquals(120L, imgReplies.get(1).getUserId());
        assertEquals(190L, imgReplies.get(2).getReply().getRno());
        assertEquals(192L, imgReplies.get(2).getUserId());
    }


}