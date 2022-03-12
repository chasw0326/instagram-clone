package com.example.instagram2.repositoryTests;

import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Likes;
import com.example.instagram2.entity.Member;
import com.example.instagram2.repository.LikesRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class LikesRepoTests {

    @Autowired
    private LikesRepository repository;

    @Disabled
    @Transactional
    @DisplayName("unlike")
    @Test
    public void unlikeTest() {
        Long tempImageId = 10L;
        Long tempMemberId = 10L;
        repository.unlike(tempImageId, tempMemberId);
    }

    @Disabled
    @Transactional
    @DisplayName("getMemberIdByImageId")
    @Test
    public void getMemberIdByImageId() {
        List<Long> expList = new ArrayList<>(Arrays.asList(50L, 34L, 3L));
        Collections.sort(expList);
        List<Long> result = repository.getMemberIdByImageId(2L);
        Collections.sort(result);
        assertEquals(expList, result);
    }

    @Disabled
    @Transactional
    @DisplayName("getLikesCntByImageId")
    @Test
    public void getLikesByImageIdTest() {
        assertEquals(2L, repository.getLikesCntByImageId(7L));
    }

    @Disabled
    @DisplayName("getMemberIdByImageId")
    @Test
    void getMemberId() {
        List<Long> expected = new ArrayList<>();
        expected.add(223L);
        assertEquals(expected, repository.getMemberIdByImageId(2L));
    }

    @Disabled
    @DisplayName("실험용 테스트")
    @Test
    public void dummyTest() {
        Likes likes = Likes.builder()
                .member(Member.builder()
                        .mno(3L)
                        .build())
                .image(Image.builder()
                        .ino(2L)
                        .build())
                .build();

        repository.save(likes);
    }

}
