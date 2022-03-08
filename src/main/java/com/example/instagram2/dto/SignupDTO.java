package com.example.instagram2.dto;

import com.example.instagram2.entity.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


@ApiModel(value="SignupDTO: 회원가입", description = "회원가입 정보들")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupDTO {

    @ApiModelProperty(value = "이메일", required = true)
    @NotEmpty(message = "이메일은 필수값입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @ApiModelProperty(value = "사용자 이름", required = true)
    @NotEmpty(message = "사용자 이름은 필수값 입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$",
            message = "사용자이름은 영어랑 숫자만 가능합니다.")
    private String username;

    @ApiModelProperty(value = "비밀번호", required = true)
    @NotEmpty
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;

    @ApiModelProperty(value = "이름")
    private String name;

    private Long id;

    public SignupDTO entityToDTO(Member member){
        return SignupDTO.builder()
                .id(member.getMno())
                .email(member.getEmail())
                .name(member.getName())
                .username(member.getUsername())
                .build();
    }
}
