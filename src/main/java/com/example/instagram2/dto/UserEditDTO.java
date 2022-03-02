package com.example.instagram2.dto;


import com.example.instagram2.entity.Member;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserEditDTO {

    private Long mno;

    @NotEmpty(message = "이름은 필수 값 입니다.")
    private String name;

    @NotEmpty(message = "사용자 이름은 필수 값 입니다.")
    private String username;

    private String website;

    private String intro;

    @NotNull(message = "이메일은 필수 값 입니다.")
    @Email
    private String email;

    @NotEmpty(message = "전화번호는 필수 값 입니다.")
    private String phone;

    @Pattern(regexp = "(남자|여자)",
            message = "남자, 여자만 가능합니다.")
    private String gender;

}
