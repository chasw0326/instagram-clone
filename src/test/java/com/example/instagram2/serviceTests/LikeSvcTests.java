package com.example.instagram2.serviceTests;


import com.example.instagram2.exception.myException.InvalidFileException;
import com.example.instagram2.repository.ImageRepository;
import com.example.instagram2.repository.LikesRepository;
import com.example.instagram2.service.LikesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class LikeSvcTests {

    @Autowired
    private LikesService likesService;

    @Autowired
    private LikesRepository likesRepository;

    @DisplayName("좋아요테이블에 좋아요 하나 추가 되는지")
    @Test
    void Should_LikeCntPlusOne_WhenLike(){
        Long imageId = 2L;
        Long userId = 223L;
        Long pastLikeCnt = likesRepository.getLikesCntByImageId(imageId);
        System.out.println("likeCnt: " + pastLikeCnt);
        likesService.like(imageId, userId);
        Long currentLikeCnt = likesRepository.getLikesCntByImageId(imageId);
        assertEquals(currentLikeCnt, pastLikeCnt + 1);
    }

    @DisplayName("Likes 테이블의 UK 잘 작동하는지")
    @Test
    void Should_ThrowException_WhenDuplicatedLike(){
        Long imageId = 2L;
        Long userId = 223L;
        Throwable ex = assertThrows(DataIntegrityViolationException.class, () -> {
            likesService.like(imageId, userId);
        });
        System.out.println(ex.getMessage());
    }

}
