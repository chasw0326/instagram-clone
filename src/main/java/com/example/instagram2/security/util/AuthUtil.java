package com.example.instagram2.security.util;

import com.example.instagram2.dto.PasswordDTO;
import com.example.instagram2.dto.SignUpDTO;
import com.example.instagram2.entity.Member;
import com.example.instagram2.entity.MemberRole;
import com.example.instagram2.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;


@Log4j2
public class AuthUtil {

    @Autowired
    private MemberRepository memberRepository;
    // BCryptPasswordEncoder
    private final PasswordEncoder passwordEncoder;
    private final PasswordUtil passwordUtil;

    public AuthUtil(PasswordEncoder passwordEncoder,
                    PasswordUtil passwordUtil){
        this.passwordEncoder = passwordEncoder;
        this.passwordUtil = passwordUtil;
    }

    @Transactional
    public Member signUp(SignUpDTO dto) {
        String rawPw = dto.getPassword();
        String email = dto.getEmail();
        String enPw = null;
        Member newMember = null;

        checkArgument(dto);

        if (passwordUtil.meter(rawPw) == PasswordStrength.STRONG) {
            enPw = passwordEncoder.encode(rawPw);
        } else {
            log.warn("Password strength is too weak");
            throw new RuntimeException("Too weak password");
        }
        if (memberRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Duplicated username");
        }

        checkEmailExistsOrMatchesPatthern(email);
        newMember = Member.builder()
                .email(email)
                .name(dto.getName())
                .username(dto.getUsername())
                .password(enPw)
                .build();

        newMember.addMemberRole(MemberRole.USER);


        memberRepository.save(newMember);

        return newMember;
    }

    @Transactional
    public Member getByCredentials(final String email, final String password){
        final Member originalMember = memberRepository.getByEmail(email);

        if(originalMember != null && passwordEncoder.matches(password, originalMember.getPassword())){
            return originalMember;
        }
        return null;
    }



    @Transactional
    public void changePassword(PasswordDTO dto) {
        Long mno = dto.getMno();
        Optional<Member> result = memberRepository.findById(mno);
        if (result.isPresent()) {
            Member member = result.get();
            if (passwordEncoder.matches(dto.getOldPw(), member.getPassword())) {
                if (passwordUtil.meter(dto.getNewPw()) == PasswordStrength.STRONG) {
                    if (dto.getNewPw().equals(dto.getCheckNewPw())) {
                        member.setPassword(passwordEncoder.encode(dto.getNewPw()));
                        memberRepository.save(member);
                    } else {
                        throw new RuntimeException("체크 비밀번호가 다릅니다.");
                    }
                } else {
                    throw new RuntimeException("비밀번호 강도가 약합니다.");
                }
            } else {
                throw new RuntimeException("현재 비밀번호랑 다릅니다.");
            }
        }
    }


    private boolean isPhoneNum(String phoneOrEmail) {
        String pattern = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$";
        if (Pattern.matches(pattern, phoneOrEmail)) {
            return true;
        }
        return false;

    }

    private boolean isEmailPattern(String email) {
        String pattern = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
        if (Pattern.matches(pattern, email)) {
            return true;
        }
        return false;

    }

    private void checkEmailExistsOrMatchesPatthern(String email) {
        if (!isEmailPattern(email)) {
            log.warn("Wrong email pattern: {}", email);
            throw new RuntimeException("Wrong email pattern");
        }
        if (memberRepository.existsByEmail(email)) {
            log.warn("Email already exists {}", email);
            throw new RuntimeException("Email already exists");
        }
    }

    private List<String> checkPhoneNumExists(String phone) {
        List<String> errMsg = new ArrayList<>();
        if (memberRepository.existsByPhoneNum(phone)) {
            log.warn("PhoneNum already exists {}", phone);
            errMsg.add("PhoneNum already exists");
                throw new RuntimeException("PhoneNum already exists");
        }
        return errMsg;
    }

    private void checkArgument(SignUpDTO dto) {
        String email = dto.getEmail().trim();
        String name = dto.getName().trim();
        String username = dto.getUsername().trim();
        List<String> errorMsg = new ArrayList<>();

        if (email.isEmpty() || email == null) {
            throw new IllegalArgumentException("Invalid email");
        }
        if (name.isEmpty() || name == null) {
            throw new IllegalArgumentException("Invalid name");
        }
        if (username.isEmpty() || username == null) {
            throw new IllegalArgumentException("Invalid username");
        }
    }
}
