package com.example.instagram2.serviceTests;


import com.example.instagram2.dto.PasswordDTO;
import com.example.instagram2.dto.SignupDTO;
import com.example.instagram2.entity.Member;
import com.example.instagram2.exception.myException.DuplicationException;
import com.example.instagram2.exception.myException.InvalidPasswordException;
import com.example.instagram2.repository.MemberRepository;
import com.example.instagram2.util.AuthUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthSvcTests {

    @Autowired
    private AuthUtil authService;

    @Autowired
    private MemberRepository repository;


    @DisplayName("회원 가입 테스트")
    @Test
    void Should_Ok_WhenSignup_WithNormalData() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
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

        Member member = authService.signup(dto);
        assertEquals(member.getEmail(), email);
        assertEquals(member.getName(), name);
        assertEquals(member.getUsername(), username);
        assertTrue(passwordEncoder.matches(password, member.getPassword()));

        repository.delete(member);
    }

    @DisplayName("중복된 사용자이름으로 가입")
    @Test
    void Should_ThrowException_WhenSignupWithDuplicatedUsername() {
        String email = getRandomStr(10) + "@naver.com";
        String name = "핫식스";
        String dupUsername = getRandomStr(10);
        String password = "abcABC123!@#";
        SignupDTO dto = SignupDTO.builder()
                .email(email)
                .name(name)
                .username(dupUsername)
                .password(password)
                .build();

        Member member = authService.signup(dto);
        SignupDTO dto2 = SignupDTO.builder()
                .email("new" + email)
                .name(name)
                .username(dupUsername)
                .password(password)
                .build();

        try {
            Throwable ex = assertThrows(DuplicationException.class, () -> {
                authService.signup(dto2);
            });
            assertEquals("Duplicated username", ex.getMessage());
        } finally {
            repository.delete(member);
        }
    }

    @DisplayName("중복된 이메일로 가입")
    @Test
    void Should_ThrowException_WhenSignupWithDuplicatedEmail() {
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

        Member member = authService.signup(dto);
        SignupDTO dto2 = SignupDTO.builder()
                .email(email)
                .name(name)
                .username("new" + username)
                .password(password)
                .build();

        try {
            Throwable ex = assertThrows(DuplicationException.class, () -> {
                authService.signup(dto2);
            });
            assertEquals("Duplicated email", ex.getMessage());
        } finally {
            repository.delete(member);
        }
    }

    @DisplayName("Entity에서 DTO로")
    @Test
    void Should_ChangeFromEntityToDTO() {
        Member member = Member.builder()
                .mno(100L)
                .email("test@naver.com")
                .name("test-name")
                .username("testUsername")
                .build();

        SignupDTO dto = authService.entityToDTO(member);
        assertEquals(dto.getId(), member.getMno());
        assertEquals(dto.getEmail(), member.getEmail());
        assertEquals(dto.getName(), member.getName());
        assertEquals(dto.getUsername(), member.getUsername());
    }

    @DisplayName("입력한 비밀번호가 이전 비밀번호와 다를때")
    @Test
    void Should_ThrowException_WhenPastPasswordIsDifferent(){
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

        Member member = authService.signup(dto);

        String newPw = "aAbBcC123!@#";
        PasswordDTO passwordDTO = PasswordDTO.builder()
                .mno(member.getMno())
                .oldPw(password + "1")
                .newPw(newPw)
                .checkNewPw(newPw)
                .build();

        try {
            Throwable ex = assertThrows(InvalidPasswordException.class, () -> {
                authService.changePassword(passwordDTO);
            });
            assertEquals("이전 비밀번호가 다릅니다.", ex.getMessage());
        } finally {
            repository.delete(member);
        }
    }

    @DisplayName("새 비밀번호 두 개가 다를때")
    @Test
    void Should_ThrowException_WhenCheckPwIsDifferent(){
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

        Member member = authService.signup(dto);

        String newPw = "aAbBcC123!@#";
        PasswordDTO passwordDTO = PasswordDTO.builder()
                .mno(member.getMno())
                .oldPw(password)
                .newPw(newPw)
                .checkNewPw(newPw + "1")
                .build();

        try {
            Throwable ex = assertThrows(InvalidPasswordException.class, () -> {
                authService.changePassword(passwordDTO);
            });
            assertEquals("확인 비밀번호가 새 비밀번호와 다릅니다.", ex.getMessage());
        } finally {
            repository.delete(member);
        }
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
