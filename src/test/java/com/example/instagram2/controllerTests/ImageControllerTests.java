package com.example.instagram2.controllerTests;


import com.example.instagram2.dto.ImageReqDTO;
import com.example.instagram2.entity.Image;
import com.example.instagram2.exception.ArgumentCheckUtil;
import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.security.service.InstaUserDetailsService;
import com.example.instagram2.service.ImageService;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;


@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ImageControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ImageService mockImageService;

    @MockBean
    private MemberService mockMemberService;

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

    @DisplayName("[Post]/image/create/style")
    @Test
    @WithUserDetails(value = "chasw@naver.com")
    void should_getFeedDTO_When_Request() throws IOException {
        MultiValueMap<String, Object> multipartData = new LinkedMultiValueMap<>();
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("testImage",
                        "testImg.png",
                        "image/png",
                        new FileInputStream("C:\\upload\\image_storage\\abc.png"));
        Resource imageResource = new ByteArrayResource(mockMultipartFile.getBytes()) {
            @Override
            public String getFilename() {
                return "image.png";
            }
        };
        multipartData.add("uploadFile", imageResource);
        multipartData.add("tags", "#뮤지컬 #레베카");
        multipartData.add("caption", "충무아트센터");
        ImageReqDTO dto = ImageReqDTO.builder()
                .uploadFile(mockMultipartFile)
                .tags("#뮤지컬 #레베카")
                .caption("충무아트센터")
                .build();
        UserDetails loggedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        given(mockImageService.uploadPicture(dto, (AuthMemberDTO) loggedUser)).willReturn(33L);

        webTestClient.post().uri("/image/create/style")
                .headers(http -> http.setBearerAuth(token))
                // 여길 bodyValue(dto)로 해도 안 됨 ㅠㅠ....
                .body(BodyInserters.fromMultipartData(multipartData))
                .exchange()
                .expectStatus().isOk();
    }
}
