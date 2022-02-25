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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;


import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;


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
    void Should_ThrowException_WhenNotExistUsername() {
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

    @DisplayName("정상적인 changePicture, [POST]/changePicture/{username}")
    @Test
    @WithUserDetails(value = "chasw@naver.com")
    void Should_isOk_WhenChangePicture() throws IOException {

        AuthMemberDTO loggedUser = (AuthMemberDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = loggedUser.getId();
        doNothing().when(mockArgumentCheckUtil).existByUsername("john");

        MultiValueMap<String, Object> multipartData = new LinkedMultiValueMap<>();
        MockMultipartFile image = new MockMultipartFile("image", "image.png", "image/png",
                "<<png data>>".getBytes());
        Resource imageResource = new ByteArrayResource(image.getBytes()) {
            @Override
            public String getFilename() {
                return "image.png";
            }
        };
        multipartData.add("imgFile", imageResource);


        given(mockMemberService.getMemberIdByUsername(loggedUser.getUsername())).willReturn(userId);
        doNothing().when(mockMemberService).changeProfilePicture(image, userId);

        webTestClient.post().uri("/user/changePicture/{username}", loggedUser.getUsername())
                .headers(http -> http.setBearerAuth(token))
                .bodyValue(multipartData)
                .exchange()
                .expectStatus().isOk();
    }

    @DisplayName("다른사람이 changePicture 시도, [POST]/changePicture/{username}")
    @Test
    @WithUserDetails(value = "chasw@naver.com")
    void Should_ThrowException_WhenChangePictureByStranger() throws IOException {

        AuthMemberDTO loggedUser = (AuthMemberDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = loggedUser.getId();
        doNothing().when(mockArgumentCheckUtil).existByUsername(loggedUser.getUsername());

        MultiValueMap<String, Object> multipartData = new LinkedMultiValueMap<>();
        MockMultipartFile image = new MockMultipartFile("image", "image.png", "image/png",
                "<<png data>>".getBytes());
        Resource imageResource = new ByteArrayResource(image.getBytes()) {
            @Override
            public String getFilename() {
                return "image.png";
            }
        };
        multipartData.add("imgFile", imageResource);

        given(mockMemberService.getMemberIdByUsername(loggedUser.getUsername())).willReturn(199L);
        doNothing().when(mockMemberService).changeProfilePicture(image, userId);

        webTestClient.post().uri("/user/changePicture/{username}", loggedUser.getUsername())
                .headers(http -> http.setBearerAuth(token))
                .bodyValue(multipartData)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @DisplayName("팔로워리스트 가져오기 [GET]/{username}/followerList")
    @Test
    @WithUserDetails(value = "chasw@naver.com")
    void Should_isOk_WhenGetFollowerList() {

        doNothing().when(mockArgumentCheckUtil).existByUsername("john");
        Pageable pageable = PageRequest.of(0, 10, Sort.by("regDate").descending());
        given(mockFollowService.getFollowList(223L, "john", pageable)).willReturn(null);

        webTestClient.get().uri("/user/{username}/followerList", "john")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk();

    }

    @DisplayName("팔로우리스트 가져오기 [GET]/{username}/followList")
    @Test
    @WithUserDetails(value = "chasw@naver.com")
    void Should_isOk_WhenGetFollowList() {

        doNothing().when(mockArgumentCheckUtil).existByUsername("john");
        Pageable pageable = PageRequest.of(0, 10, Sort.by("regDate").descending());
        given(mockFollowService.getFollowList(223L, "john", pageable)).willReturn(null);

        webTestClient.get().uri("/user/{username}/followerList", "john")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk();

    }
}
