package com.example.instagram2.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ApiModel(value="PasswordDTO: 비밀번호 변경 DTO", description = "비밀번호변경 화면값들과 DTO")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDTO {

    @ApiModelProperty(value = "멤버 번호")
    Long mno;

    @ApiModelProperty(value = "프로필 사진")
    String imgUrl;

    @ApiModelProperty(value = "사용자 이름")
    String username;

    @ApiModelProperty(value = "이전 비밀번호", required = true)
    @NotEmpty(message = "이전 비밀번호 값이 없습니다.")
    String oldPw;

    @ApiModelProperty(value = "새 비밀번호", required = true)
    @NotNull(message = "새 비밀번호 값이 없습니다.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    String newPw;

    @ApiModelProperty(value = "확인 비밀번호", required = true)
    @NotEmpty(message = "새 비밀번호 확인값이 없습니다.")
    String checkNewPw;

}
