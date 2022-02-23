package com.example.instagram2.controllerTests;

import com.example.instagram2.exception.ArgumentCheckUtil;
import com.example.instagram2.service.ImageService;
import com.example.instagram2.service.LikesService;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LikesControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private LikesService mockLikesService;

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

    @DisplayName("존재하지않는 imageId를 좋아요 ,[Post]/likes/{imageId}")
    @Test
    @WithUserDetails(value = "chasw@naver.com")
    void Should_ThrowException_WhenLikeNotExistImageId(){

        doThrow(new IllegalArgumentException("존재하지 않는 memberId 입니다. 입력한 값:" + 9999999999L))
                .when(mockArgumentCheckUtil).existByImageId(9999999999L);

        webTestClient.post().uri("/likes/9999999999")
                .headers(http -> http.setBearerAuth(token))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody()
                .jsonPath("exception").isEqualTo("IllegalArgumentException");
    }

    @DisplayName("존재하지않는 imageId를 좋아요 취소 ,[Delete]/likes/{imageId}")
    @Test
    @WithUserDetails(value = "chasw@naver.com")
    void Should_ThrowException_WhenUnlikeNotExistImageId(){

        doThrow(new IllegalArgumentException("존재하지 않는 memberId 입니다. 입력한 값:" + 9999999999L))
                .when(mockArgumentCheckUtil).existByImageId(9999999999L);

        webTestClient.delete().uri("/likes/9999999999")
                .headers(http -> http.setBearerAuth(token))
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody()
                .jsonPath("exception").isEqualTo("IllegalArgumentException");
    }
}