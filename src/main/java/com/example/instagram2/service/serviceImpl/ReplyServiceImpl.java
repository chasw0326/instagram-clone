package com.example.instagram2.service.serviceImpl;

import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.service.ReplyService;
import com.example.instagram2.dto.ReplyReqDTO;
import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Reply;
import com.example.instagram2.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        log.info(dto);
        try {
            Reply reply = dtoToEntity(dto, authMember);
            log.info(reply);
            repository.save(reply);
            return reply.getRno();
        } catch (Exception e) {
            log.error("error deleting reply");
            throw new RuntimeException("error deleting reply");
        }
    }

    @Override
    @Transactional
    public List<ReplyReqDTO> getList(Long ino, Pageable pageable) {

        List<Reply> result = repository
                .getRepliesByImageOrderByRegDate(Image.builder().ino(ino).build(),
                                                pageable);

        log.info(result.toString());

        return result.stream().map(reply -> entityToDTO(reply)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void remove(Long rno, Long userId) {
        try {
            Optional<Reply> result = repository.findById(userId);
            if (result.isPresent()) {
                Reply reply = result.get();
                log.info(reply);
                if (reply.getMember().getMno().equals(userId)) {
                    repository.deleteById(rno);
                }
            }
        } catch (Exception e) {
            log.error("error deleting reply");
            throw new RuntimeException("error deleting reply");
        }
    }

}
