package com.example.instagram2.service.serviceImpl;

import com.example.instagram2.service.LikesService;
import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Likes;
import com.example.instagram2.entity.Member;
import com.example.instagram2.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
public class LikeServiceImpl implements LikesService {

    private final LikesRepository repository;

    @Transactional
    public void like(Long imageId, Long userId){

        Image image = Image.builder()
                .ino(imageId)
                .build();
        Member member = Member.builder()
                .mno(userId)
                .build();
        Likes like = Likes.builder()
                .image(image)
                .member(member)
                .build();

        log.info("------------좋아요------------");
        log.info(like);
        repository.save(like);
    }

    @Transactional
    public void undoLike(Long imageId, Long userId){
        log.info("------------좋아요 취소------------");
        log.info(imageId);
        log.info(userId);
        repository.unlike(imageId, userId);
    }

}
