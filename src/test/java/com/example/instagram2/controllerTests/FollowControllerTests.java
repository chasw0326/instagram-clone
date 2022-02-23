package com.example.instagram2.controllerTests;


import com.example.instagram2.exception.ArgumentCheckUtil;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class FollowControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ArgumentCheckUtil mockArgumentCheckUtil;

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

    @DisplayName("존재하지 않는 유저를 팔로우 했을때, [Post]/follow/{toMemberId}")
    @Test
    @WithUserDetails(value = "chasw@naver.com")
    void Should_ThrowException_WhenFollowNotExistUser() {
        Long toMemberId = 987654321L;
        doThrow(new IllegalArgumentException("존재하지 않는 memberId 입니다. 입력한 값:" + toMemberId))
                .when(mockArgumentCheckUtil).existByMemberId(toMemberId);

        webTestClient.post().uri("/follow/987654321")
                .headers(http -> http.setBearerAuth(token))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody()
                .jsonPath("message").isEqualTo("존재하지 않는 memberId 입니다. 입력한 값:" + toMemberId)
                .jsonPath("exception").isEqualTo("IllegalArgumentException");
    }

    @DisplayName("존재하지 않는 유저를 언팔로우 했을때, [Delete]/follow/{toMemberId}")
    @Test
    @WithUserDetails(value = "chasw@naver.com")
    void Should_ThrowException_WhenUnfollowNotExistUser() {

        Long toMemberId = 987654321L;
        doThrow(new IllegalArgumentException("존재하지 않는 memberId 입니다. 입력한 값:" + toMemberId))
                .doNothing().when(mockArgumentCheckUtil).existByMemberId(toMemberId);

        webTestClient.post().uri("/follow/987654321")
                .headers(http -> http.setBearerAuth(token))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody()
                .jsonPath("message").isEqualTo("존재하지 않는 memberId 입니다. 입력한 값:" + toMemberId)
                .jsonPath("exception").isEqualTo("IllegalArgumentException");
    }

}

