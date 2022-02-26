package com.example.instagram2.serviceTests;


import com.example.instagram2.dto.SignupDTO;
import com.example.instagram2.dto.UserEditDTO;
import com.example.instagram2.entity.Member;
import com.example.instagram2.exception.myException.DuplicationException;
import com.example.instagram2.exception.myException.IllegalFileException;
import com.example.instagram2.repository.MemberRepository;
import com.example.instagram2.service.MemberService;
import com.example.instagram2.service.serviceImpl.AuthUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import javax.swing.text.html.Option;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MemberSvcTests {

    @Autowired
    private MemberService memberService;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private MemberRepository memberRepository;


    @DisplayName("업로드 사진이 null로 회원사진 변경, changeProfilePicture")
    @Test
    void Should_ThrowException_WhenFileIsNull(){
        Long userId = 223L;
        Throwable ex = assertThrows(IllegalFileException.class, () -> {
            memberService.changeProfilePicture(null, userId);
        });
        assertEquals("uploadFile is null", ex.getMessage());
    }

    @DisplayName("회원사진 변경, changeProfilePicture")
    @Test
    void Should_ChangePicture() throws IOException {
        MockMultipartFile file = new MockMultipartFile("testImage",
                "testImg.png",
                "image/png",
                new FileInputStream("C:\\upload\\image_storage\\abc.png"));

        Long userId = 223L;
        memberService.changeProfilePicture(file, userId);
        Optional<Member> result =  memberRepository.findById(userId);
        if(result.isPresent()){
            Member member = result.get();
            System.out.println(member.getProfileImageUrl());
        }
    }

    @DisplayName("회원사진 삭제 , deleteProfilePicture")
    @Test
    void Should_DeletePicture(){
        Long userId = 223L;
        memberService.deleteProfilePicture(userId);
        Optional<Member> result = memberRepository.findById(userId);
        if(result.isPresent()){
            Member member = result.get();
            assertNull(member.getProfileImageUrl());
        }
    }

    @DisplayName("없는유저정보 요청시도")
    @Test
    void Should_ThrowException_WhenNotExistUser(){
        String email = getRandomStr(10) + "@naver.com";
        String name = "핫식스";
        String username = getRandomStr(10);
        String password = "abcABC123!@#";
        SignupDTO dto = SignupDTO.builder()
                .email(email)
                .name(name)
                .username(username)
                .password(password)
                .build();

        // 가입후
        Member member = authUtil.signup(dto);
        // pk얻고
        Long userId = member.getMno();
        // 가입한 멤버 삭제
        memberRepository.delete(member);
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> {
            memberService.getMemberInfo(userId);
        });
    }


    @DisplayName("중복된 사용자 이름으로 회원 정보 수정, modifyMemberInfo")
    @Test
    void modify() {
        UserEditDTO dto = UserEditDTO.builder()
                .email("3F3074W0PK@naver.com")
                .name("핫식스")
                .username("danxiya")
                .build();

        Throwable ex = assertThrows(DuplicationException.class, () -> {
            memberService.modifyMemberInfo(227L, dto);
        });
        assertEquals("중복된 사용자 이름 입니다.", ex.getMessage());
    }

    public static String getRandomStr(int size) {
        if (size > 0) {
            char[] tmp = new char[size];
            for (int i = 0; i < tmp.length; i++) {
                int div = (int) Math.floor(Math.random() * 2);
                if (div == 0) {
                    tmp[i] = (char) (Math.random() * 10 + '0');
                } else {
                    tmp[i] = (char) (Math.random() * 26 + 'A');
                }
            }
            return new String(tmp);
        }
        return "ERROR : Size is required.";
    }
}
