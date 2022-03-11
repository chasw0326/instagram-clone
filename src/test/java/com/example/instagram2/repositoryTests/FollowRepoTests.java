package com.example.instagram2.repositoryTests;


import com.example.instagram2.entity.Member;
import com.example.instagram2.repository.FollowRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FollowRepoTests {

    @Autowired
    private FollowRepository repository;


    @Disabled
    @DisplayName("getFollowCount")
    @Test
    void verifyFollowCount() {
        Long fromMemberId = 9L;
        while (true) {
            if (repository.existsByFromMember_Mno(fromMemberId))
                fromMemberId = (long) (Math.random() * 100) + 1;
            else
                break;
        }

        System.out.println("-------------------------------------");
        System.out.println("테스트용 fromMemberId: " + fromMemberId);


        List<Long> temp = new ArrayList<>();
        for (int i = 0; i < 10; i++) { // 10개 생성
            Long followeeId = (long) (Math.random() * 100) + 1;
            temp.add(followeeId);
            repository.follow(fromMemberId, followeeId);
        }
        try {
            Long testCount = repository.getFollowCount(fromMemberId);
            System.out.println("테스트 카운트 개수: " + testCount);
            assertEquals(10L, testCount);
        } finally {
            for (Long tempId : temp) {
                repository.unFollow(fromMemberId, tempId);
            }
        }
    }

    @Disabled
    @DisplayName("getFollowerTest")
    @Test
    void veryfiFollowerCount() {
        Long toMemberId = 70L;
        while (true) {
            if (repository.existsByToMember_Mno(toMemberId))
                toMemberId = (long) (Math.random() * 100) + 1;
            else
                break;
        }

        System.out.println("-------------------------------------");
        System.out.println("테스트용 toMemberId: " + toMemberId);


        List<Long> temp = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Long followerId = (long) (Math.random() * 100) + 1;
            temp.add(followerId);
            repository.follow(followerId, toMemberId);
        }
        System.out.println("팔로워Id:" + temp);

        try {
            Long testCount = repository.getFollowerCount(toMemberId);
            System.out.println("testCount: " + testCount);
            assertEquals(10L, testCount);
        } finally {
            for (Long tempId : temp) {
                repository.unFollow(tempId, toMemberId);
            }
        }
    }

    @Disabled
    @DisplayName("follow")
    @Test
    public void followTest() {
        int count = (int) (Math.random() * 3) + 1;

        for (int j = 0; j < count; j++) {
            Long followerId = (long) (Math.random() * 100) + 1;
            IntStream.rangeClosed(1, 5).forEach(i -> {
                Long followeeId = (long) (Math.random() * 100) + 1;
                repository.follow(followerId, followeeId);
            });
        }
    }

    @Disabled
    @DisplayName("unfollow")
    @Test
    public void unFollowTest() {
        Long followerId = 65L;
        Long followeeId = 89L;

        repository.unFollow(followerId, followeeId);
    }

    @Disabled
    @DisplayName("followState")
    @Test
    public void followState() {
        Long existFromMemberId = 3L;
        Long existToMemberId = 63L;   // 데베에 존재하는 값
        Long notExistFromMemberId = 3L;
        Long notExistToMemberId = 64L; // 데베에 존재하지 않는 값
        assertEquals(1, repository.followState(
                existFromMemberId, existToMemberId),"팔로우 상태");
        assertEquals(0, repository.followState(
                notExistFromMemberId, notExistToMemberId), "팔로우 상태 아님");
    }

    @Disabled
    @DisplayName("existsTest")
    @Test
    public void existByToMemberTest() {
        Long existMemberId = 4L;
        Long notExistMemberId = 10L;

        System.out.println(repository.existsByToMember_Mno(existMemberId));
        assertEquals(true, repository.existsByToMember_Mno(existMemberId));
        System.out.println(repository.existsByToMember_Mno(existMemberId));
        assertEquals(false, repository.existsByToMember_Mno(notExistMemberId));
    }

    @Disabled
    @DisplayName("getFollowerData")
    @Test
    public void followerListTest(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("regDate").descending());
        Page<Object[]> result = repository.getFollowerData("63번이름", pageable);

        System.out.println("---------------------------팔로워 정보---------------------------");
        for (Object[] arr : result){
            System.out.println(Arrays.toString(arr));
        }
    }

    @Disabled
    @DisplayName("getFollowData")
    @Test
    public void followeeListTest(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("regDate").descending());
        Page<Object[]> result = repository.getFollowData("63번이름", pageable);

        System.out.println("---------------------------내가 팔로우하는사람 정보---------------------------");
        for (Object[] arr : result){
            System.out.println(Arrays.toString(arr));
        }
    }

    @Disabled("실험용 테스트...")
    @Test
    public void dummyTest(){
//        repository.follow(1L, 6L);
        System.out.println("팔로우 수: " + repository.getFollowCount(99L));
        System.out.println("팔로워 수: " + repository.getFollowerCount(43L));
    }
}
