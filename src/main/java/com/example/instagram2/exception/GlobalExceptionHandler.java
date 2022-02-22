package com.example.instagram2.exception;

import com.example.instagram2.exception.myException.DuplicationException;
import com.example.instagram2.exception.myException.IllegalFileException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({DuplicationException.class})
    public ResponseEntity<?> DuplicatedArgs(DuplicationException e) {
        log.error("DuplicationException: {}", e.getMessage());
        e.printStackTrace();
        ErrorRespDTO dto = errorToDTO(e);
        return new ResponseEntity<>(dto, HttpStatus.CONFLICT);
    }

//    @ExceptionHandler({BadCredentialsException.class})
//    public ResponseEntity<?> BadCredential(BadCredentialsException e){
//        log.error("login error: {}", e.getMessage());
//        e.printStackTrace();
//        ErrorDTO dto = errorToDTO(e);
//        return new ResponseEntity<>(dto, HttpStatus.UNAUTHORIZED);
//    }

    @ExceptionHandler({IllegalFileException.class})
    public ResponseEntity<?> IllegalFileType(IllegalFileException e) {
        log.error("IllegalFileType: {}", e.getMessage());
        e.printStackTrace();
        ErrorRespDTO dto = errorToDTO(e);
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<?> IllegalArgumentHandler(IllegalArgumentException e) {
        log.error("IllegalArgumentException: {}", e.getMessage());
        e.printStackTrace();
        ErrorRespDTO dto = errorToDTO(e);
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<?> RuntimeHandler(RuntimeException e) {
        log.error("RuntimeException: {}", e.getMessage());
        e.printStackTrace();
        ErrorRespDTO dto = errorToDTO(e);
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            MethodArgumentConversionNotSupportedException.class})
    public ResponseEntity<?> validException(BindException e) {
        log.error("valid Error: {}", e.getMessage());
        e.printStackTrace();
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));

        ErrorRespDTO dto = ErrorRespDTO.builder()
                .exception(e.getClass().getSimpleName())
                .errors(errors)
                .build();
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    private ErrorRespDTO errorToDTO(Exception e) {
        return ErrorRespDTO.builder()
                .exception(e.getClass().getSimpleName())
                .message(e.getMessage())
                .build();
    }
}
