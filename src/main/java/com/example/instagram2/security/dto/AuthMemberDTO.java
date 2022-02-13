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

@Log4j2
@Getter
@Setter
@ToString
public class AuthMemberDTO extends User implements OAuth2User {

    private String email;

    private String password;

    private String name;

    private boolean fromSocial;

    private Long id;

    private Map<String, Object> attr;

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

//    public AuthMemberDTO(
//            Member member,
//            String username,
//            String password,
//            boolean fromSocial,
//            Collection<? extends GrantedAuthority> authorities,) {
//
//        super(username, password, authorities);
//        this.member = member;
//        this.fromSocial = fromSocial;
//    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attr;
    }
}