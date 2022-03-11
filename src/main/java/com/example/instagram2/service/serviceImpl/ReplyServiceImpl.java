package com.example.instagram2.service.serviceImpl;

import com.example.instagram2.dto.ImgReply;
import com.example.instagram2.exception.myException.NoAuthorityException;
import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.service.ReplyService;
import com.example.instagram2.dto.ReplyReqDTO;
import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Reply;
import com.example.instagram2.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <code>ReplyService</code><br>
 * 멤버에 관련된 서비스
 *
 * @author chasw326
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository repository;

    /**
     * 댓글 등록
     * @param dto (dto with 댓글 내용)
     * @see com.example.instagram2.dto.ReplyReqDTO
     * @param authMember (로그인한 유저)
     * @return 댓글id
     */
    @Override
    @Transactional
    public Long register(ReplyReqDTO dto, AuthMemberDTO authMember) {
        log.info("{} try to register reply", authMember.getEmail());
        Reply reply = dtoToEntity(dto, authMember);
        repository.save(reply);
        return reply.getRno();
    }

    /**
     * 글에 등록된 댓글 리스트들 가져오기<br>
     * 등록시간기준 오름차순으로
     * @param ino
     * @param pageable
     * @return
     */
    @Override
    @Transactional
    public List<ReplyReqDTO> getReplyList(Long ino, Pageable pageable) {
        log.info("imageId: {}의 댓글들", ino);

        List<Reply> result = repository.getRepliesByImage_InoOrderByRegDateAsc(ino, pageable);

        return result.stream().map(reply -> entityToDTO(reply)).collect(Collectors.toList());
    }

    /**
     * 댓글 지우기
     * @param rno
     * @param userId
     * 본인이 아니면 예외던짐
     * @throws NoAuthorityException
     */
    @Override
    @Transactional(rollbackFor = {NoAuthorityException.class})
    public void remove(Long rno, Long userId) throws NoAuthorityException {
        Optional<Reply> result = repository.findById(rno);
        if (result.isPresent()) {
            Reply reply = result.get();
            if (!reply.getMember().getMno().equals(userId)) {
                log.error("error deleting reply by userId: {}", userId);
                throw new NoAuthorityException("userId: " + userId + " has no Authority to delete reply");
            }
            repository.deleteById(rno);
        }
    }

    /**
     * 피드에서는 전체 댓글 가져오기엔 너무 리소스가 크기때문에<br>
     * 최근에 등록된 댓글 3개를 가져온다.<br>
     * 댓글 전체를 보려면 댓글 더보기를 통해 나머지 댓글들도 가져온다.
     * @see com.example.instagram2.controller.ReplyController#getAllReply(Long, Pageable)
     * @param ino
     * @return
     */
    @Override
    @Transactional
    public List<ImgReply> get3Replies(Long ino){
        List<Reply> replies = repository.getTop3ByImage_InoOrderByRegDate(ino);
        List<ImgReply> imgReplies = new ArrayList<>();
        for(Reply r : replies){
            ImgReply imgReply = ImgReply.builder()
                    .reply(r)
                    .username(r.getMember().getUsername())
                    .userId(r.getMember().getMno())
                    .build();
            imgReplies.add(imgReply);
        }
        return imgReplies;
    }
}

