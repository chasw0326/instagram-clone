package com.example.instagram2.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@ApiModel(value="FollowRespDTO", description = "다른사람 페이지 팔로우 리스트에서 그 사람들과 나의 팔로우 관계")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class FollowRespDTO {

    @ApiModelProperty(value = "사용자id")
    private Long userId;

    @ApiModelProperty(value = "사용자이름")
    private String username;

    @ApiModelProperty(value = "프로필 사진")
    private String profileImageUrl;

    @ApiModelProperty(value = "내가 팔로우 하는지")
    private int followState;

    @ApiModelProperty(value = "서로 팔로우 상태인지")
    private int f4fState;
}
