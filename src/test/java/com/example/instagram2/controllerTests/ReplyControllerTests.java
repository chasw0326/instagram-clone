package com.example.instagram2.controllerTests;

import com.example.instagram2.exception.ArgumentCheckUtil;
import com.example.instagram2.exception.myException.NoAuthorityException;
import com.example.instagram2.service.ReplyService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ReplyControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ArgumentCheckUtil mockArgumentCheckUtil;

    @MockBean
    private ReplyService mockReplyService;

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

    @Test
    @WithUserDetails(value = "chasw@naver.com")
    void should_getReplies_When_NormalRequest(){
        Long imageId = 100L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("regDate").descending());
        doNothing().when(mockArgumentCheckUtil).existByImageId(imageId);
        given(mockReplyService.getList(imageId, pageable)).willReturn(null);

        webTestClient.get().uri("/reply/{imageId}" , imageId)
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk();

    }

    @DisplayName("정상적인 댓글삭제, [GET]/user/reply/{imageId}")
    @Test
    @WithUserDetails(value = "chasw@naver.com")
    void should_DeleteReply_When_NormalRequest() throws NoAuthorityException {
        Long imageId = 100L;

        doNothing().when(mockArgumentCheckUtil).existByImageId(imageId);
        doNothing().when(mockReplyService).remove(imageId, 223L);

        webTestClient.delete().uri("/reply/{imageId}" , imageId)
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk();
    }

    @DisplayName("다른사람이 댓글삭제 시도, [GET]/user/reply/{imageId}")
    @Test
    @WithUserDetails(value = "chasw@naver.com")
    void should_ThrowException_When_DeleteByStranger() throws NoAuthorityException {
        Long imageId = 100L;

        doNothing().when(mockArgumentCheckUtil).existByImageId(imageId);
        doThrow(new NoAuthorityException("권한없음")).when(mockReplyService).remove(imageId, 2233L);

        webTestClient.delete().uri("/reply/{imageId}" , imageId)
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("exception").isEqualTo("NoAuthorityException");
    }
}
