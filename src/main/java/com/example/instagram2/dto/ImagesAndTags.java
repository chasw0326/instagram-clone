package com.example.instagram2.dto;

import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Reply;
import com.example.instagram2.entity.Tag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@ApiModel(value="피드로 보내줄 값들", description = "이미지들과 댓글, 태그들")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ImagesAndTags {

    @ApiModelProperty(value = "멤버 아이디")
    Long memberId;

    @ApiModelProperty(value = "사용자 이름")
    String username;

    @ApiModelProperty(value = "이미지")
    Image images;

    @ApiModelProperty(value = "이미지에 달린 태그들")
    List<Tag> tags;

    @ApiModelProperty(value = "이미지에 달린 댓글들")
    List<ImgReply> replies;

}
