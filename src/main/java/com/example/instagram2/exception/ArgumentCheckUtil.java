package com.example.instagram2.exception;


import com.example.instagram2.exception.myException.DuplicationException;
import com.example.instagram2.repository.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <code>ArgumentCheckUtil</code><br>
 * 값들이 유효한지 체크하는 책임을 가진 클래스
 * 컨트롤러에서 바로 repository로 가는것이 싫어서 단계를 나누었습니다.
 * @author chasw326
 */
@Log4j2
public class ArgumentCheckUtil {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ImageRepository imageRepository;

    /**
     * 유저네임이 존재하는지
     * @param username
     */
    public void existByUsername(String username) {
        if (username == null || !memberRepository.existsByUsername(username)) {
            log.warn("Unknown user: {}", username);
            throw new IllegalArgumentException("존재하지 않는 username 입니다. 입력한 값: " + username);
        }
    }

    /**
     * 이미지가 존재하는지
     * @param ino
     */
    public void existByImageId(Long ino) {
        if (ino == null || !imageRepository.existsByIno(ino)) {
            log.warn("Invalid imageId: {}", ino);
            throw new IllegalArgumentException("존재하지 않는 imageId 입니다. 입력한 값: " + ino);
        }
    }

    /**
     * 멤버가 존재하는지
     * @param memberId
     */
    public void existByMemberId(Long memberId) {
        if (memberId == null || !memberRepository.existsById(memberId)) {
            log.warn("Invalid memberId: {}", memberId);
            throw new IllegalArgumentException("존재하지 않는 memberId 입니다. 입력한 값: " + memberId);
        }
    }

}
