package com.example.instagram2.dto;


import com.example.instagram2.entity.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ApiModel(value="UserEditDTO", description = "회원정보 수정")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserEditDTO {

    @ApiModelProperty(value = "멤버 id", required = true)
    private Long mno;

    @ApiModelProperty(value = "이름", required = true)
    @NotEmpty(message = "이름은 필수 값 입니다.")
    private String name;

    @ApiModelProperty(value = "사용자 이름", required = true)
    @NotEmpty(message = "사용자 이름은 필수 값 입니다.")
    private String username;

    @ApiModelProperty(value = "웹사이트")
    private String website;

    @ApiModelProperty(value = "자기소개")
    private String intro;

    @ApiModelProperty(value = "이메일", required = true)
    @NotNull(message = "이메일은 필수 값 입니다.")
    @Email
    private String email;

    @ApiModelProperty(value = "전화번호", required = true)
    @NotEmpty(message = "전화번호는 필수 값 입니다.")
    private String phone;

    @ApiModelProperty(value = "성별")
    @Pattern(regexp = "(남자|여자)",
            message = "남자, 여자만 가능합니다.")
    private String gender;

}
