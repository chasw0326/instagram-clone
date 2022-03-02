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

@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository repository;

    @Override
    @Transactional
    public Long register(ReplyReqDTO dto, AuthMemberDTO authMember) {
        log.info("{} try to register reply", authMember.getEmail());
        Reply reply = dtoToEntity(dto, authMember);
        repository.save(reply);
        return reply.getRno();
    }

    @Override
    @Transactional
    public List<ReplyReqDTO> getList(Long ino, Pageable pageable) {
        log.info("imageId: {}의 댓글들", ino);
        Image image = Image.builder()
                .ino(ino)
                .build();
        List<Reply> result = repository.getRepliesByImageOrderByRegDateAsc(image, pageable);

        return result.stream().map(reply -> entityToDTO(reply)).collect(Collectors.toList());
    }

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

