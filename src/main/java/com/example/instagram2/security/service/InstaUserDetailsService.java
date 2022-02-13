package com.example.instagram2.security.service;

import com.example.instagram2.entity.Member;
import com.example.instagram2.repository.MemberRepository;
import com.example.instagram2.security.dto.AuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class InstaUserDetailsService implements UserDetailsService {

    private final MemberRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        log.info("Login in progress..................");
        log.info("InstaUserDetailsService loadUserByUsername " + username);

        Optional<Member> result = repository.findByEmailAndSocial(username, false);

        if(!result.isPresent()){
            throw new UsernameNotFoundException("Check Email or Social ");
        }

        Member member = result.get();

        log.info("-----------------------------");
        log.info(member);

        AuthMemberDTO authMember = new AuthMemberDTO(
                member.getEmail(),
                member.getPassword(),
                member.isFromSocial(),
                member.getRoleSet().stream()
                        .map(role-> new SimpleGrantedAuthority
                                ("ROLE_"+role.name())).collect(Collectors.toSet()),
                member.getMno()
        );
        authMember.setName(member.getName());

        return authMember;
    }
}
