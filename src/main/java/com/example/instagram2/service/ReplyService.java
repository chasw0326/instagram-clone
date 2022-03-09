package com.example.instagram2.service;


import com.example.instagram2.dto.ImgReply;
import com.example.instagram2.dto.ReplyReqDTO;
import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Member;
import com.example.instagram2.entity.Reply;
import com.example.instagram2.exception.myException.NoAuthorityException;
import com.example.instagram2.security.dto.AuthMemberDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**<strong>구현클래스에 문서화</strong><br>
 * {@link com.example.instagram2.service.serviceImpl.ReplyServiceImpl}
 */
public interface ReplyService {

    Long register(ReplyReqDTO replyReqDTO, AuthMemberDTO authMember);

    List<ReplyReqDTO> getReplyList(Long ino, Pageable pageable);

    List<ImgReply> get3Replies(Long ino);

    void remove(Long rno, Long userId) throws NoAuthorityException;

    default Reply dtoToEntity(ReplyReqDTO replyReqDTO, AuthMemberDTO authMember) {

        Image image = Image.builder()
                .ino(replyReqDTO.getIno())
                .build();

        Member member = Member.builder()
                .mno(authMember.getId())
                .build();

        Reply reply = Reply.builder()
                .content(replyReqDTO.getText())
                .image(image)
                .member(member)
                .build();

        return reply;
    }

    default ReplyReqDTO entityToDTO(Reply reply){

        ReplyReqDTO dto = ReplyReqDTO.builder()
                .rno(reply.getRno())
                .ino(reply.getImage().getIno())
                .mno(reply.getMember().getMno())
                .text(reply.getContent())
                .regDate(reply.getRegDate())
                .modDate(reply.getModDate())
                .build();

        return dto;
    }


}
