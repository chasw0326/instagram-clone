package com.example.instagram2.repositoryTests;


import com.example.instagram2.entity.Member;
import com.example.instagram2.repository.MemberRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MemberRepoTests {

    @Autowired
    private MemberRepository repository;

    @Disabled
    @DisplayName("findByEmailAndSocial")
    @Test
    public void findByUserNameTest() {
        String username = "1번이름";
        Optional<Member> result = repository.findByEmailAndSocial(username, false);
        if (result.isPresent()) {
            Member member = result.get();
            System.out.println("member = " + member.toString());
        }
    }

    @Test
    void findByUsername() {
        Optional<Member> result = repository.findByUsername("john");
        if (result.isPresent()) {
            Member member = result.get();
            assertEquals(223L, member.getMno());
        }
    }

    @Test
    public void findByEmail() {
        Optional<Member> result = repository.findByEmail("bcaaeaa@naver.com");
        if (result.isPresent()) {
            Member member = result.get();
            System.out.println("member= " + member);
            assertEquals(215, member.getMno());
        }
    }

    @Test
    public void findByEmailAndSocial() {
        Optional<Member> result = repository.findByEmailAndSocial("bcaa@naver.com", false);
        if (result.isPresent()) {
            Member member = result.get();
            System.out.println("member= " + member);
        }
    }

    @Test
    void getProfileImageAndUsernameById() {
        Object[] arr = (Object[]) repository.getProfileImagAndUsernameById(219L);
        String profileImg = (String) arr[0];
        String username = (String) arr[1];
        assertEquals("hehe", username);
        assertEquals("imgUrlsfjkslfsdkfjl", profileImg);
    }

    @Test
    void getProfileImageById() {
        //imgUrlsfjkslfsdkfjl
        assertEquals("imgUrlsfjkslfsdkfjl", repository.getProfileImageById(219L));
    }
}
