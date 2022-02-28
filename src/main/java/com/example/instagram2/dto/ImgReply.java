package com.example.instagram2.dto;

import com.example.instagram2.entity.Reply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ImgReply {

    Long userId;

    String username;

    Reply reply;
}
