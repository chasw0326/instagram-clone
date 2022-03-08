package com.example.instagram2.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;


@ApiModel(value="FeedDTO: 피드 리스트", description = "내 프로필 사진과, 내가 팔로우하는 사람들의 글들")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class FeedDTO {

    @ApiModelProperty(value = "내 프로필 사진")
    private String myPicture;

    @ApiModelProperty(value = "피드의 모든 데이터들")
    private List<ImagesAndTags> imagesAndTags;
}
