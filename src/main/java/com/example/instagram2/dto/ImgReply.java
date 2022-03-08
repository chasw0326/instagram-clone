package com.example.instagram2.dto;

import com.example.instagram2.entity.Reply;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@ApiModel(value="ImgReply", description = "글에 달린 댓글 정보들")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ImgReply {

    @ApiModelProperty(value = "유저 아이디")
    Long userId;

    @ApiModelProperty(value = "사용자이름")
    String username;

    @ApiModelProperty(value = "댓글")
    Reply reply;
}
