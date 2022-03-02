package com.example.instagram2.dto;

import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Reply;
import com.example.instagram2.entity.Tag;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ImagesAndTags {

    Long memberId;

    String username;

    Image images;

    List<Tag> tags;

    List<ImgReply> replies;

}
