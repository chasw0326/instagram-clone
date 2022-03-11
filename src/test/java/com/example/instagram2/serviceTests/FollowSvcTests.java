package com.example.instagram2.serviceTests;

import com.example.instagram2.dto.FollowRespDTO;
import com.example.instagram2.repository.FollowRepository;
import com.example.instagram2.service.FollowService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class FollowSvcTests {

    @Autowired
    private FollowService followService;

    @Autowired
    private FollowRepository followRepository;

    @Test
    void Should_GetFollowerListTest() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        //63 91
        List<FollowRespDTO> result = followService.getFollowerList(100L, "96username", pageRequest);
        System.out.println(result.toString());
        assertEquals(2, result.size());
        assertEquals(63L, result.get(0).getUserId());
        assertEquals(91L, result.get(1).getUserId());
    }

    @Test
    void Should_GetFollowList() {
        PageRequest pageRequest = PageRequest.of(0, 20);
        List<FollowRespDTO> result = followService.getFollowList(100L, "john", pageRequest);
        for (FollowRespDTO dto : result) {
            System.out.println(dto);
        }
    }

    @Test
    public void unfollowTest() {
        followService.unFollow(223L, 1L);

        assertEquals(0, followRepository.followState(223L, 1L));
    }

    @Test
    public void follow() {

        followService.follow(1L, 200L);
        assertTrue(followRepository.followState(1L, 200L) > 0);
    }
}
