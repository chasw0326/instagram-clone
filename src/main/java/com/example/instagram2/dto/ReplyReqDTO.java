package com.example.instagram2.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReplyReqDTO {

    private Long rno;

    @NotEmpty(message = "댓글 내용을 입력하세요.")
    private String text;

    private Long mno;

    private Long ino;

    private LocalDateTime regDate, modDate;
}
