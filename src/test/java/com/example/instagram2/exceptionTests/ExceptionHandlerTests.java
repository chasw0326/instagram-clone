package com.example.instagram2.exceptionTests;

import com.example.instagram2.exception.GlobalExceptionHandler;
import com.example.instagram2.exception.myException.DuplicationException;
import com.example.instagram2.exception.myException.InvalidFileException;
import com.example.instagram2.exception.myException.InvalidPasswordException;
import com.example.instagram2.exception.myException.NoAuthorityException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;


import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ExceptionHandlerTests {

    @Autowired
    private GlobalExceptionHandler exceptionHandler;

    @Test
    void verifyDuplicatedArgsHandler(){
        ResponseEntity<?> result = exceptionHandler.duplicatedArgsHandler(new DuplicationException());
        assertEquals("409 CONFLICT", result.getStatusCode().toString());
    }

    @Test
    void verifyPasswordHandler(){
        ResponseEntity<?> result = exceptionHandler.passwordHandler(new InvalidPasswordException());
        assertEquals("400 BAD_REQUEST", result.getStatusCode().toString());
    }

    @Test
    void verifySqlErrorHandler(){
        ResponseEntity<?> result = exceptionHandler.sqlErrorhandler(new DataIntegrityViolationException(null));
        assertEquals("400 BAD_REQUEST", result.getStatusCode().toString());
    }

    @Test
    void verifyNoAuthorityHandler(){
        ResponseEntity<?> result = exceptionHandler.noAuthorityHandler(new NoAuthorityException());
        assertEquals("401 UNAUTHORIZED", result.getStatusCode().toString());
    }

    @Test
    void verifyIllegalFileTypeHandler(){
        ResponseEntity<?> result = exceptionHandler.illegalFileTypeHandler(new InvalidFileException());
        assertEquals("400 BAD_REQUEST", result.getStatusCode().toString());
    }

    @Test
    void verifyIllegalArgumentHandler(){
        ResponseEntity<?> result = exceptionHandler.illegalArgumentHandler(new IllegalArgumentException());
        assertEquals("400 BAD_REQUEST", result.getStatusCode().toString());
    }

    @Test
    void verifyRuntimeHandler(){
        ResponseEntity<?> result = exceptionHandler.runtimeHandler(new RuntimeException());
        assertEquals("400 BAD_REQUEST", result.getStatusCode().toString());
    }

}
