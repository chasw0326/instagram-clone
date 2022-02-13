package com.example.instagram2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDTO {

    private String email;

    private String name;

    private String username;

    private String password;

    private String token;

    private Long id;
}
