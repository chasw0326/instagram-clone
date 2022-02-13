package com.example.instagram2.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReplyReqDTO {

    private Long rno;

    private String text;

    private Long mno;

    private Long ino;

    private LocalDateTime regDate, modDate;
}
