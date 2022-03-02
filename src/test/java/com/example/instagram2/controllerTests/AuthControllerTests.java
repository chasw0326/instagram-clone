package com.example.instagram2.controllerTests;


import com.example.instagram2.dto.PasswordDTO;
import com.example.instagram2.dto.SignupDTO;
import com.example.instagram2.dto.UserEditDTO;
import com.example.instagram2.entity.Member;
import com.example.instagram2.exception.myException.InvalidPasswordException;
import com.example.instagram2.service.MemberService;
import com.example.instagram2.service.serviceImpl.AuthUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalTime;
import java.util.UUID;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthUtil mockAuthUtil;

    @MockBean
    private MemberService mockMemberService;

    private String token;

    @Nested
    @DisplayName("Jwt이 필요한 테스트들")
    class testsWithJwt {

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

        @DisplayName("비밀번호 변경화면 값 테스트 [GET]/accounts/password/change")
        @Test
        @WithUserDetails(value = "chasw@naver.com")
        void Should_GetPasswordDTO_WhenRequestPwChangeScreen() {
            PasswordDTO dto = PasswordDTO.builder()
                    .imgUrl("testingImgUrl")
                    .username("john")
                    .build();
            given(mockMemberService.getProfileImgUrlAndUsernameById(223L)).willReturn(dto);

            webTestClient.get().uri("/accounts/password/change")
                    .headers(http -> http.setBearerAuth(token))
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().valueEquals("Content-Type", "application/json")
                    .expectBody()
                    .jsonPath("imgUrl").isEqualTo("testingImgUrl")
                    .jsonPath("username").isEqualTo("john");
        }

        @DisplayName("유저정보 수정 테스트, [Get]/accounts/edit")
        @Test
        @WithUserDetails(value = "chasw@naver.com")
        void Should_Get_UserEditDTO_WhenRequestEdit() {
            UserEditDTO dto = UserEditDTO.builder()
                    .mno(223L)
                    .name("로니콜먼")
                    .username("john")
                    .website("www.myInfo.com")
                    .intro("나는 누구인가")
                    .email("chasw@naver.com")
                    .phone("01075319842")
                    .gender("남자")
                    .build();

            given(mockMemberService.getMemberInfo(223L)).willReturn(dto);
            webTestClient.get().uri("/accounts/edit")
                    .headers(http -> http.setBearerAuth(token))
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().valueEquals("Content-Type", "application/json");

        }

        @DisplayName("비밀번호 변경 테스트")
        @Test
        @WithUserDetails(value = "whoamI@naver.com")
        void Should_ChangePassword() throws InvalidPasswordException {
            PasswordDTO passwordDTO = PasswordDTO.builder()
                    .oldPw("abcABC123!@#")
                    .checkNewPw("abcABC123!@")
                    .newPw("abcABC123!@")
                    .build();
            doNothing().when(mockAuthUtil).changePassword(passwordDTO);

            webTestClient.post().uri("/accounts/password/change")
                    .headers(http -> http.setBearerAuth(token))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .bodyValue(passwordDTO)
                    .exchange()
                    .expectStatus().isOk();
        }

        @DisplayName("유저정보 수정 테스트 [Put]/accounts/edit")
        @Test
        @WithUserDetails(value = "chasw@naver.com")
        void Should_GetIsOk_WhenEditUserinfo() {
            UserEditDTO dto = UserEditDTO.builder()
                    .mno(223L)
                    .name("제이커틀러")  //from 로니콜먼 to 제이커틀러
                    .username("john")
                    .website("www.myInfo.com")
                    .intro("나는 누구인가")
                    .email("chasw@naver.com")
                    .phone("01075319842")
                    .gender("남자")
                    .build();


            webTestClient.put().uri("/accounts/edit")
                    .headers(http -> http.setBearerAuth(token))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .bodyValue(dto)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().valueEquals("Content-Type", "application/json");
        }
    }

    @Nested
    @DisplayName("Jwt필요없는 테스트들")
    class testsWithoutJwt {

        @Nested
        @DisplayName("회원가입 테스트들")
        class signupTests {

            @DisplayName("잘못된 이메일형식으로 회원가입, [Post]/accounts/signup")
            @Test
            void should_ThrowException_When_SignupWithInvalidEmail() {

                SignupDTO dto = SignupDTO.builder()
                        .email("ctrlTest")
                        .name(null)
                        .username("helloworld")
                        .password("abcABC123!@#")
                        .build();

                webTestClient.post().uri("/accounts/signup")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .bodyValue(dto)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody()
                        .jsonPath("exception").isEqualTo("MethodArgumentNotValidException");

            }

            @DisplayName("약한강도 비밀번호로 회원가입, [Post]/accounts/signup")
            @Test
            void should_ThrowException_When_SignupWithInvalidPassword() {

                SignupDTO dto = SignupDTO.builder()
                        .email("0222@naver.com")
                        .name(null)
                        .username("useruser")
                        .password("abcABC123")
                        .build();

                webTestClient.post().uri("/accounts/signup")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .bodyValue(dto)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody()
                        .jsonPath("exception").isEqualTo("MethodArgumentNotValidException");
                ;
            }

            @DisplayName("null 비밀번호로 회원가입, [Post]/accounts/signup")
            @Test
            void should_ThrowException_When_SignupWithEmptyPassword() {

                SignupDTO dto = SignupDTO.builder()
                        .email("0222@naver.com")
                        .name(null)
                        .username("useruser")
                        .password(null)
                        .build();

                webTestClient.post().uri("/accounts/signup")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .bodyValue(dto)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody()
                        .jsonPath("exception").isEqualTo("MethodArgumentNotValidException");
                ;
            }

            @DisplayName("한글 사용자이름으로 회원가입, [Post]/accounts/signup")
            @Test
            void should_ThrowException_When_SignupWithkoreanUsername() {

                SignupDTO dto = SignupDTO.builder()
                        .email("bcaaeeee@naver.com")
                        .name(null)
                        .username("나는한글사용자")
                        .password("abcABC123!@#")
                        .build();

                webTestClient.post().uri("/accounts/signup")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .bodyValue(dto)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody()
                        .jsonPath("exception").isEqualTo("MethodArgumentNotValidException");
            }

            @DisplayName("정상값으로 회원가입, [Post]/accounts/signup")
            @Test
            void should_isOk_When_SignupWithValidArgs() {
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                LocalTime now = LocalTime.now();
                int hour = now.getHour();
                int minute = now.getMinute();
                int second = now.getSecond();
                String timeStr = "member" + hour + minute + second;
                Member member = Member.builder()
                        .mno(1000000000L)
                        .email(timeStr + "@naver.com")
                        .password(passwordEncoder.encode("abcABC123"))
                        .name(null)
                        .username(timeStr)
                        .build();

                SignupDTO dto = SignupDTO.builder()
                        .email(timeStr + "@naver.com")
                        .name(null)
                        .username(timeStr)
                        .password("abcABC123!@#")
                        .build();

                SignupDTO respUserDTO = SignupDTO.builder()
                        .id(member.getMno())
                        .email(member.getEmail())
                        .name(member.getName())
                        .username(member.getUsername())
                        .build();

                given(mockAuthUtil.signup(dto)).willReturn(member);
                given(mockAuthUtil.entityToDTO(member)).willReturn(respUserDTO);

                webTestClient.post().uri("/accounts/signup")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .bodyValue(dto)
                        .exchange()
                        .expectStatus().isOk();
            }

            @DisplayName("틀린 비밀번호로 로그인시도, [Post]/login")
            @Test
            void should_ThrowUnauthorized_WhenInvalidPassword() throws JSONException {
                String postRequestBody = new JSONObject()
                        .put("email", "chasw@naver.com")
                        .put("password", "wrongPassword")
                        .toString();

                webTestClient.post().uri("/login")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .bodyValue(postRequestBody)
                        .exchange()
                        .expectStatus().isUnauthorized()
                        .expectHeader().valueEquals("Content-Type", "application/json;charset=utf-8")
                        .expectBody()
                        .jsonPath("message").isEqualTo("Bad credentials");
            }

        }
    }
}
