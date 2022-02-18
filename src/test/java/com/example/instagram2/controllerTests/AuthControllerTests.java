package com.example.instagram2.controllerTests;


import com.example.instagram2.dto.SignUpDTO;
import com.example.instagram2.entity.Member;
import com.example.instagram2.repository.MemberRepository;
import com.example.instagram2.service.MemberService;
import com.example.instagram2.service.serviceImpl.AuthUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthUtil mockAuthUtil;

    @MockBean
    private MemberService mockMemberService;

    @DisplayName("회원가입 테스트")
    @Test
    void verifySignup() throws Exception {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Member member = Member.builder()
                .mno(100L)
                .email("ctrlTest@naver.com")
                .password(passwordEncoder.encode("abcABC123"))
                .name(null)
                .username("ctrlTest")
                .build();

        SignUpDTO dto = SignUpDTO.builder()
                .email("ctrlTest@naver.com")
                .name(null)
                .username("ctrlTest")
                .password("abcABC123!@#")
                .build();

        SignUpDTO respUserDTO = SignUpDTO.builder()
                .id(member.getMno())
                .email(member.getEmail())
                .name(member.getName())
                .username(member.getUsername())
                .build();

        given(mockAuthUtil.signUp(dto)).willReturn(member);
        given(mockAuthUtil.entityToDTO(member)).willReturn(respUserDTO);

        webTestClient.post().uri("/accounts/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody()
                .jsonPath("username").isEqualTo("ctrlTest")
                .jsonPath("id").isEqualTo(100L)
                .jsonPath("email").isEqualTo("ctrlTest@naver.com");
    }


    @DisplayName("요청값 없이 로그인 시도")
    @Test
    void verifyLogin() {
        webTestClient.post().uri("/accounts/login")
                .exchange()
                .expectStatus().isForbidden();
    }


}
