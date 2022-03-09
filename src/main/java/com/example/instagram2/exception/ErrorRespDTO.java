package com.example.instagram2.exception;


import lombok.*;

import java.util.Map;

/**
 * 에러메시지 DTO
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorRespDTO {

    String exception;

    String message;

    // 아이디 비밀번호가 동시에 틀리는 등, 여러 예외가 동시에 발생할때를 위한 변수
    Map<String, String> errors;

    }

