package com.example.instagram2.dtoTests;

import com.example.instagram2.dto.*;
import com.example.instagram2.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class DtoTests {

    @Test
    void verifyFollowRespDTO() {
        FollowRespDTO followRespDTO = FollowRespDTO.builder()
                .userId(1L)
                .username("username")
                .profileImageUrl("imageUrl")
                .followState(0)
                .f4fState(0)
                .build();

        assertEquals(1L, followRespDTO.getUserId());
        assertEquals("username", followRespDTO.getUsername());
        assertEquals("imageUrl", followRespDTO.getProfileImageUrl());
        assertEquals(0, followRespDTO.getFollowState());
        assertEquals(0, followRespDTO.getF4fState());


        FollowRespDTO followRespDTO1 = new FollowRespDTO();

        followRespDTO1.setUserId(2L);
        followRespDTO1.setUsername("username2");
        followRespDTO1.setProfileImageUrl("url");
        followRespDTO1.setFollowState(1);
        followRespDTO1.setF4fState(1);

        assertEquals(2L, followRespDTO1.getUserId());
        assertEquals("username2", followRespDTO1.getUsername());
        assertEquals("url", followRespDTO1.getProfileImageUrl());
        assertEquals(1, followRespDTO1.getFollowState());
        assertEquals(1, followRespDTO1.getF4fState());
    }

    @Test
    void verifyPasswordDTO() {
        PasswordDTO passwordDTO = PasswordDTO.builder()
                .mno(1L)
                .imgUrl("url")
                .username("username")
                .oldPw("abcABC123!@#$")
                .newPw("abcABC123!@#")
                .checkNewPw("abcABC123!@#")
                .build();

        assertEquals(1L, passwordDTO.getMno());
        assertEquals("url", passwordDTO.getImgUrl());
        assertEquals("username", passwordDTO.getUsername());
        assertEquals("abcABC123!@#$", passwordDTO.getOldPw());
        assertEquals("abcABC123!@#", passwordDTO.getNewPw());
        assertEquals("abcABC123!@#", passwordDTO.getCheckNewPw());

        PasswordDTO passwordDTO1 = new PasswordDTO();

        passwordDTO1.setMno(2L);
        passwordDTO1.setImgUrl("newUrl");
        passwordDTO1.setUsername("newUsername");
        passwordDTO1.setOldPw("ABCabc!@#123");
        passwordDTO1.setNewPw("ABCabc!@#1232");
        passwordDTO1.setCheckNewPw("ABCabc!@#1232");

        assertEquals(2L, passwordDTO1.getMno());
        assertEquals("newUrl", passwordDTO1.getImgUrl());
        assertEquals("newUsername", passwordDTO1.getUsername());
        assertEquals("ABCabc!@#123", passwordDTO1.getOldPw());
        assertEquals("ABCabc!@#1232", passwordDTO1.getNewPw());
        assertEquals("ABCabc!@#1232", passwordDTO1.getCheckNewPw());
    }

    @Test
    void verifySignupDTO(){
        SignupDTO signupDTO = SignupDTO.builder()
                .email("chasw@naver.com")
                .username("username")
                .password("abcABC123!@#")
                .name("name")
                .id(1L)
                .build();

        assertEquals("chasw@naver.com", signupDTO.getEmail());
        assertEquals("username", signupDTO.getUsername());
        assertEquals("abcABC123!@#", signupDTO.getPassword());
        assertEquals("name", signupDTO.getName());
        assertEquals(1L, signupDTO.getId());

    }

    @Test
    void verifyResponseDTO(){
        List<Object> data = new ArrayList<>();
        data.add("data");
        ResponseDTO<?> responseDTO = ResponseDTO.builder()
                .error("error")
                .data(data)
                .build();

        assertEquals("error", responseDTO.getError());
        assertEquals("data", responseDTO.getData().get(0));

        List<?> data1 = new ArrayList<>();
        data.add("data1");
        ResponseDTO<?> responseDTO1 = new ResponseDTO<>();
        responseDTO1.setError("error1");
    }

    @Test
    void verifyUserProfileRespDTO(){
        UserProfileRespDTO userProfileRespDTO = new UserProfileRespDTO();
        userProfileRespDTO.setFollowState(false);
        userProfileRespDTO.setMyself(false);
        userProfileRespDTO.setFollowCount(1L);
        userProfileRespDTO.setFollowerCount(1L);
        userProfileRespDTO.setImageCount(1L);
        List<String> imgUrlList = new ArrayList<>();
        imgUrlList.add("url");
        imgUrlList.add("url2");
        userProfileRespDTO.setImgUrlList(imgUrlList);
        userProfileRespDTO.setMember(Member.builder().mno(1L).build());

        assertFalse(userProfileRespDTO.isFollowState());
        assertFalse(userProfileRespDTO.isMyself());
        assertEquals(1L, userProfileRespDTO.getFollowCount());
        assertEquals(1L, userProfileRespDTO.getFollowerCount());
        assertEquals(1L, userProfileRespDTO.getImageCount());
        assertEquals("url", userProfileRespDTO.getImgUrlList().get(0));
        assertEquals("url2", userProfileRespDTO.getImgUrlList().get(1));
        assertEquals(1L, userProfileRespDTO.getMember().getMno());

    }
}
