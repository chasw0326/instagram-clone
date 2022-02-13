package com.example.instagram2.repositoryTests;

import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Likes;
import com.example.instagram2.entity.Member;
import com.example.instagram2.repository.LikesRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class LikesRepoTests {

    @Autowired
    private LikesRepository repository;

    @Test
    public void unlikeTest(){
        Long tempImageId = 10L;
        Long tempMemberId = 10L;
        repository.unlike(tempImageId, tempMemberId);
    }

    @Test
    public void getMemberIdByImageId(){
        List<Long> expList = new ArrayList<>(Arrays.asList(50L, 34L, 3L));
        Collections.sort(expList);
        List<Long> result = repository.getMemberIdByImageId(2L);
        Collections.sort(result);
        assertEquals(expList, result);
    }

    @Test
    public void getLikesByImageIdTest(){
        assertEquals(2L,repository.getLikesCntByImageId(7L));
    }

    @Test
    public void dummyTest(){
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
