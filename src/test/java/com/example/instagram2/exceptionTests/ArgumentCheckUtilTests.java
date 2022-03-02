package com.example.instagram2.exceptionTests;

import com.example.instagram2.exception.ArgumentCheckUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class ArgumentCheckUtilTests {

    @Autowired
    private ArgumentCheckUtil argumentCheckUtil;

    @Test
    void Should_NotThrowException_WhenUserIsExistByUsername(){
        String username = "asdf";
        assertDoesNotThrow(() -> argumentCheckUtil.existByUsername(username));
    }

    @Test
    void Should_ThrowException_WhenUserIsNotExistByUsername(){
        String username = "asdff";
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> {
            argumentCheckUtil.existByUsername(username);
        });
        assertEquals("존재하지 않는 username 입니다. 입력한 값: " + username, ex.getMessage());
    }

    @Test
    void Should_NotThrowException_WhenImageIdIsExist(){
        Long imageId = 16L;
        assertDoesNotThrow(() -> argumentCheckUtil.existByImageId(imageId));
    }

    @Test
    void Should_ThrowException_WhenImageIdIsNotExist(){
        Long imageId = 0L;
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> {
            argumentCheckUtil.existByImageId(imageId);
        });
        assertEquals("존재하지 않는 imageId 입니다. 입력한 값: " + imageId, ex.getMessage());
    }

    @Test
    void Should_NotThrowException_WhenUserIsExistByMemberId(){
        Long memberId = 224L;
        assertDoesNotThrow(() -> argumentCheckUtil.existByMemberId(memberId));
    }

    @Test
    void Should_ThrowException_WhenUserIsNotExistByMemberID(){
        Long memberId = 0L;
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> {
            argumentCheckUtil.existByMemberId(memberId);
        });
        assertEquals("존재하지 않는 memberId 입니다. 입력한 값: " + memberId, ex.getMessage());
    }
}
