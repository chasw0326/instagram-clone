package com.example.instagram2.security.service;

import com.example.instagram2.entity.Member;
import com.example.instagram2.entity.MemberRole;
import com.example.instagram2.repository.MemberRepository;
import com.example.instagram2.security.dto.AuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <code>InstaOAuth2UserDetailsService</code><br>
 * 인증받은 소셜로그인 유저를 만들어 줍니다.
 * @author chasw326
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class InstaOAuth2UserDetailsService extends DefaultOAuth2UserService {

    private final MemberRepository repository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 현재는 구글만 지원합니다.<br>
     * AuthMemberDTO로 변환합니다.
     * @param userRequest
     * @return AuthMemberDTO
     * @see com.example.instagram2.security.dto.AuthMemberDTO
     * @throws OAuth2AuthenticationException
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("------------------------------------");
        log.info("OAuth Login in progress......................");
        log.info("userRequest: " + userRequest);

        String clientName = userRequest.getClientRegistration().getClientName();

        log.info("clientName: " + clientName);
        log.info(userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("===================================");
        oAuth2User.getAttributes().forEach((k, v) -> {
            log.info(k + ":" + v);
        });

        String email = null;

        if (clientName.equals("Google")) {
            email = oAuth2User.getAttribute("email");
        }
        log.info("EMAIL: " + email);

        Member member = saveSocialMember(email);

        AuthMemberDTO authMember = new AuthMemberDTO(
                member.getEmail(),
                member.getPassword(),
                member.isFromSocial(),
                member.getRoleSet().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toList()),
                member.getMno(),
                oAuth2User.getAttributes()
        );
        authMember.setName(member.getName());

        return authMember;
    }

    /**
     * 이미 존재하면 존재하는 값을 가져옵니다.<br>
     * 비밀번호 1111로 가입하고, LoginSuccessHandler에서<br>
     * 회원정보 및 비밀번호 수정 화면으로 redirect 할 것입니다.
     * @param email
     * @return Member
     */
    private Member saveSocialMember(String email) {
        Optional<Member> result = repository.findByEmailAndSocial(email, true);

        if (result.isPresent()) {
            return result.get();
        }

        String uuid = UUID.randomUUID().toString();
        uuid = uuid.substring(1,15);
        Member member = Member.builder()
                .email(email)
                .username(uuid)
                .name(email)
                .password(passwordEncoder.encode("1111"))
                .fromSocial(true)
                .build();

        member.addMemberRole(MemberRole.USER);
        repository.save(member);
        return member;
    }
}
