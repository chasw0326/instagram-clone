package com.example.instagram2.service.serviceImpl;

import com.example.instagram2.dto.PasswordDTO;
import com.example.instagram2.dto.SignupDTO;
import com.example.instagram2.entity.Member;
import com.example.instagram2.entity.MemberRole;
import com.example.instagram2.exception.myException.DuplicationException;
import com.example.instagram2.repository.MemberRepository;
import com.example.instagram2.security.util.PasswordUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Log4j2
public class AuthUtil {

    @Autowired
    private MemberRepository memberRepository;
    // BCryptPasswordEncoder
    private final PasswordEncoder passwordEncoder;
    private final PasswordUtil passwordUtil;

    public AuthUtil(PasswordEncoder passwordEncoder,
                    PasswordUtil passwordUtil) {
        this.passwordEncoder = passwordEncoder;
        this.passwordUtil = passwordUtil;
    }

    @Transactional
    public Member signup(final SignupDTO dto) {
        log.info("signup email: {}", dto.getEmail());
        String rawPw = dto.getPassword();
        String email = dto.getEmail();

        if (memberRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicationException("Duplicated username");
        }
        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicationException("Duplicated email");
        }

        Member newMember = Member.builder()
                .email(email)
                .name(dto.getName())
                .username(dto.getUsername())
                .password(passwordEncoder.encode(rawPw))
                .build();

        newMember.addMemberRole(MemberRole.USER);

        memberRepository.save(newMember);

        return newMember;
    }

    @Transactional
    public SignupDTO entityToDTO(Member member){
        return SignupDTO.builder()
                .id(member.getMno())
                .email(member.getEmail())
                .name(member.getName())
                .username(member.getUsername())
                .build();
    }


    @Transactional
    public void changePassword(final PasswordDTO dto) {
        Long mno = dto.getMno();
        Optional<Member> result = memberRepository.findById(mno);
        if (!result.isPresent()) {
            log.warn("result is empty");
            throw new IllegalArgumentException("멤버id를 확인하세요.");
        }
        Member member = result.get();
        if (!passwordEncoder.matches(dto.getOldPw(), member.getPassword())) {
            log.warn("이전 비밀번호가 다릅니다.");
            throw new RuntimeException("이전 비밀번호가 다릅니다.");
        }
        if (!dto.getNewPw().equals(dto.getCheckNewPw())) {
            log.warn("확인 비밀번호가 새 비밀번호와 다릅니다.");
            throw new RuntimeException("확인 비밀번호가 새 비밀번호와 다릅니다.");
        }
        member.setPassword(passwordEncoder.encode(dto.getNewPw()));
        memberRepository.save(member);
        log.info("changePassword");
    }
}