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

/**
 * <code>LikeService</code><br>
 * 좋아요 서비스
 *
 * @author chasw326
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class LikeServiceImpl implements LikesService {

    private final LikesRepository repository;

    /**
     * 좋아요 += 1 <br>
     * UK 제약조건을 설정해서 같은 유저가 좋아요 두번 누를수 없음
     * @param imageId (이미지 id)
     * @param userId (좋아요 누를 유저)
     */
    @Override
    @Transactional
    public void like(Long imageId, Long userId) {

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
        log.info("imageId: {}", imageId);
        log.info("userId: {}", userId);
        log.info("-----------------------------");
        repository.save(like);
    }

    /**
     * 좋아요 -= 1 <br>
     * 좋아요 취소
     * @param imageId (이미지 id)
     * @param userId (좋아요 취소할 유저)
     */
    @Override
    @Transactional
    public void undoLike(Long imageId, Long userId) {
        log.info("------------좋아요 취소------------");
        log.info("imageId: {}", imageId);
        log.info("userId: {}", userId);
        repository.unlike(imageId, userId);
    }

}
