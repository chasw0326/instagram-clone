package com.example.instagram2.controllerTests;

import com.example.instagram2.exception.ArgumentCheckUtil;
import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.service.FollowService;
import com.example.instagram2.service.MemberService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MemberControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private FollowService mockFollowService;

    @MockBean
    private ArgumentCheckUtil mockArgumentCheckUtil;

    @MockBean
    private MemberService mockMemberService;

    private String token;

    @BeforeEach
    @Test
    void Should_Provide_JWT() throws JSONException {
        String postRequestBody = new JSONObject()
                .put("email", "chasw@naver.com")
                .put("password", "abcABC123!@#")
                .toString();


        token = WebClient.create()
                .post()
                .uri("localhost:" + port + "/login")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .bodyValue(postRequestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(token);
    }

    @DisplayName("존재하지 않는 username으로 요청")
    @Test
    @WithUserDetails(value = "chasw@naver.com")
    void Should_ThrowException_WhenNotExistUsername(){
        doThrow(new IllegalArgumentException("존재하지 않는 username 입니다. 입력한 값:" + "hotSix"))
                .when(mockArgumentCheckUtil).existByUsername("hotSix");

        webTestClient.get().uri("/user/hotSix")
                .headers(http -> http.setBearerAuth(token))
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody()
                .jsonPath("exception").isEqualTo("IllegalArgumentException");
    }

    @DisplayName("존재하지 않는 username으로 요청")
    @Test
    @WithUserDetails(value = "chasw@naver.com")
    void Should_ThrowException_WhenPrincipalIsNotMe(){
        doThrow(new IllegalArgumentException("존재하지 않는 username 입니다. 입력한 값:" + "hotSix"))
                .when(mockArgumentCheckUtil).existByUsername("hotSix");

        UserDetails loggedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(loggedUser);

        given(mockMemberService.getMemberIdByUsername("hotSix")).willReturn(201L);

//        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "excel.xlsx", "multipart/form-data", is);

        webTestClient.put().uri("/user/hotSix/changePicture")
                .headers(http -> http.setBearerAuth(token))
                .contentType(MULTIPART_FORM_DATA)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody()
                .jsonPath("exception").isEqualTo("IllegalArgumentException");
    }
}
