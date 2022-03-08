package com.example.instagram2.dto;


import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;


@ApiModel(value="UserProfile 다른사람이 볼수있는 유저 정보들", description = "팔로우상태, 팔로워 팔로우 수, 글들")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserProfileRespDTO {

    @ApiModelProperty(value = "내가 팔로우 했는지")
    private boolean followState;

    @ApiModelProperty(value = "팔로우 수")
    private Long followCount;

    @ApiModelProperty(value = "팔로워 수")
    private Long followerCount;

    @ApiModelProperty(value = "이미지 개수")
    private Long imageCount;

    @ApiModelProperty(value = "멤버 정보")
    private Member member;

    @ApiModelProperty(value = "이미지 리스트")
    private List<String> imgUrlList;

    @ApiModelProperty(value = "본인인지")
    private boolean myself; //본인인지 확인
}
