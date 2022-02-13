package com.example.instagram2.serviceTests;

import com.example.instagram2.service.FollowService;
import com.example.instagram2.service.serviceImpl.FollowServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FollowSvcTests {

    @Autowired
    private FollowService followService;

//    @Test
//    public void getFollowListTest(){
//        System.out.println(followService.getFollowerList(33L, 36L));
//    }

    @Test
    public void followTest(){
        followService.follow(1L, 2L);
    }

    @Test
    public void unfollowTest(){
        followService.unFollow(1L, 2L);

    }

}
