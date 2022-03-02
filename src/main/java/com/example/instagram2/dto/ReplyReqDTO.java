package com.example.instagram2.dto;


import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ReplyReqDTO {

    private Long rno;

    @NotEmpty(message = "댓글 내용을 입력하세요.")
    private String text;

    private Long mno;

    private Long ino;

    private LocalDateTime regDate, modDate;
}
