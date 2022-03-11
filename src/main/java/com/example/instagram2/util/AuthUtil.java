package com.example.instagram2.util;

import com.example.instagram2.dto.PasswordDTO;
import com.example.instagram2.dto.SignupDTO;
import com.example.instagram2.entity.Member;
import com.example.instagram2.entity.MemberRole;
import com.example.instagram2.exception.myException.DuplicationException;
import com.example.instagram2.exception.myException.InvalidPasswordException;
import com.example.instagram2.repository.MemberRepository;
import com.example.instagram2.security.util.PasswordUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * <code>AuthUtil</code><br>
 * 비밀번호, 회원가입등 중요한 서비스들 <br>
 * 설정파일에 bean으로 등록함
 * @author chasw326
 */
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

    /**
     * 회원가입<br>
     * 중복체크 한다.<br>
     * Role_User로 가입된다.
     * @param dto 이메일, 비밀번호, 사용자이름
     * @return Member
     */
    @Transactional(rollbackFor = {DuplicationException.class})
    public Member signup(final SignupDTO dto) {
        log.info("signup email: {}", dto.getEmail());
        log.info("signup username: {}", dto.getUsername());

        if (memberRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicationException("Duplicated username");
        }
        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicationException("Duplicated email");
        }

        Member newMember = Member.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();


        newMember.addMemberRole(MemberRole.USER);

        memberRepository.save(newMember);

        return newMember;
    }

    /**
     * 비밀번호 변경<br>
     * 비밀번호 강도는 dto에서 정규표현식으로 체크한다.<br>
     * 입력한 비밀번호들이 올바른지 체크한다.
     * @param dto
     * 예외메시지 보다 예외자체를 프론트에서 파싱하기 위해 <br>
     * 새로 커스텀예외를 만들었다.
     * @throws InvalidPasswordException
     */
    @Transactional(rollbackFor = {
            IllegalArgumentException.class,
            InvalidPasswordException.class})
    public void changePassword(final PasswordDTO dto) throws InvalidPasswordException {
        Long mno = dto.getMno();
        Optional<Member> result = memberRepository.findById(mno);
        if (!result.isPresent()) {
            log.warn("result is empty, input: {}", dto.getMno());
            throw new IllegalArgumentException("멤버id를 확인하세요. 입력한 값:" + dto.getMno());
        }
        Member member = result.get();
        if (!passwordEncoder.matches(dto.getOldPw(), member.getPassword())) {
            log.warn("이전 비밀번호가 다릅니다.");
            throw new InvalidPasswordException("이전 비밀번호가 다릅니다.");
        }
        if (!dto.getNewPw().equals(dto.getCheckNewPw())) {
            log.warn("확인 비밀번호가 새 비밀번호와 다릅니다.");
            throw new InvalidPasswordException("확인 비밀번호가 새 비밀번호와 다릅니다.");
        }
        member.setPassword(passwordEncoder.encode(dto.getNewPw()));
        memberRepository.save(member);
        log.info("changePassword");
    }

    public SignupDTO entityToDTO(Member member) {
        return SignupDTO.builder()
                .id(member.getMno())
                .email(member.getEmail())
                .name(member.getName())
                .username(member.getUsername())
                .build();
    }
}