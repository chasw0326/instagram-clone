package com.example.instagram2.dto;

import com.example.instagram2.entity.Reply;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ImgReply {

    Long userId;

    String username;

    Reply reply;
}
