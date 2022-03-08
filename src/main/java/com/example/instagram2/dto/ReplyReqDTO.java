package com.example.instagram2.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ApiModel(value="ReplyDTO", description = "댓글 내용 입력")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ReplyReqDTO {

    private Long rno;

    @ApiModelProperty(value = "댓글 내용", required = true)
    @NotEmpty(message = "댓글 내용을 입력하세요.")
    private String text;

    @ApiModelProperty(value = "멤버 번호", required = false)
    private Long mno;

    @ApiModelProperty(value = "이미지 번호", required = true)
    private Long ino;

    private LocalDateTime regDate, modDate;
}
