package com.example.instagram2.security.dto;

import com.example.instagram2.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <code>AuthMemberDTO</code><br>
 * jwt확인 후 인증유저로 바꿔줍니다.
 * @author chasw326
 */
@Log4j2
@Getter
@Setter
@ToString
public class AuthMemberDTO extends User implements OAuth2User {

    // User, OAuth2User의 username 으로 email을 사용한다.
    private String email;

    private String password;

    private String name;

    private boolean fromSocial;

    private Long id;

    private Map<String, Object> attr;

    /**
     * 소셜로그인 -> 인증유저
     * @param username email
     * @param password
     * @param fromSocial
     * @param authorities
     * @param id
     * @param attr 소셜로그인 정보들
     */
    public AuthMemberDTO(
            String username,
            String password,
            boolean fromSocial,
            Collection<? extends GrantedAuthority> authorities,
            Long id,
            Map<String, Object> attr) {

        this(username, password, fromSocial, authorities, id);
        this.attr = attr;
    }

    /**
     * 일반로그인 -> 인증유저
     * @param username email
     * @param password
     * @param fromSocial
     * @param authorities
     * @param id
     */
    public AuthMemberDTO(
            String username,
            String password,
            boolean fromSocial,
            Collection<? extends GrantedAuthority> authorities,
            Long id) {

        super(username, password, authorities);
        this.email = username;
        this.password = password;
        this.fromSocial = fromSocial;
        this.id = id;
    }

    /**
     * 소셜 로그인 정보들
     * @return
     */
    @Override
    public Map<String, Object> getAttributes() {
        return this.attr;
    }
}