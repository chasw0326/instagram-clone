package com.example.instagram2.exception;


import com.example.instagram2.exception.myException.DuplicationException;
import com.example.instagram2.repository.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

@Log4j2
public class ArgumentCheckUtil {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ImageRepository imageRepository;

    public void existByUsername(String username) {
        if (username == null || !memberRepository.existsByUsername(username)) {
            log.warn("Unknown user: {}", username);
            throw new IllegalArgumentException("존재하지 않는 username 입니다. 입력한 값: " + username);
        }
    }

    public void existByImageId(Long ino) {
        if (ino == null || !imageRepository.existsByIno(ino)) {
            log.warn("Invalid imageId: {}", ino);
            throw new IllegalArgumentException("존재하지 않는 imageId 입니다. 입력한 값: " + ino);
        }
    }

    public void existByMemberId(Long memberId) {
        if (memberId == null || !memberRepository.existsById(memberId)) {
            log.warn("Invalid memberId: {}", memberId);
            throw new IllegalArgumentException("존재하지 않는 memberId 입니다. 입력한 값: " + memberId);
        }
    }

//    public void checkDuplicatedUsername(String username){
//        if(memberRepository.existsByUsername(username)){
//            log.warn("Duplicated username: {}", username);
//            throw new DuplicationException("중복된 사용자 이름 입니다. 입력한 값:" + username);
//        }
//    }
}
