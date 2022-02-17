package com.example.instagram2.dto;

import com.example.instagram2.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDTO {

    @NotNull(message = "이메일은 필수값입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    private String name;

    @NotEmpty(message = "사용자 이름은 필수값 입니다.")
    private String username;

    @NotNull
    @Pattern(regexp = "/^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}$/",
            message = "소문자, 숫자, 특수문자를 조합해야합니다.")
    private String password;

    private String token;

    private Long id;

    public SignUpDTO entityToDTO(Member member){
        return SignUpDTO.builder()
                .id(member.getMno())
                .email(member.getEmail())
                .name(member.getName())
                .username(member.getUsername())
                .build();
    }
}
