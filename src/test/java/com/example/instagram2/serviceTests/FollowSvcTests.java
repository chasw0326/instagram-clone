package com.example.instagram2.serviceTests;

import com.example.instagram2.dto.FollowRespDTO;
import com.example.instagram2.repository.FollowRepository;
import com.example.instagram2.service.FollowService;
import com.example.instagram2.service.serviceImpl.FollowServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FollowSvcTests {

    @Autowired
    private FollowService followService;

    @Test
    void Should_GetFollowerListTest(){
        PageRequest pageRequest = PageRequest.of(0,10);
        //63 91
        List<FollowRespDTO> result = followService.getFollowerList(100L, "96username", pageRequest);
        assertEquals(63L, result.get(0).getUserId());
        assertEquals(91L, result.get(0).getUserId());
    }

    @Test
    void Should_GetFollowList(){
        PageRequest pageRequest = PageRequest.of(0,20);
        List<FollowRespDTO> result = followService.getFollowList(100L, "john", pageRequest);
        for(FollowRespDTO dto : result){
            System.out.println(dto);
        }
    }

    @Test
    public void unfollowTest(){
        followService.unFollow(223L, 1L);
    }

    @Test
    public void follow(){
        for(int i = 0; i<10; i++) {
            followService.follow(1L, 2L);
        }
    }
}
