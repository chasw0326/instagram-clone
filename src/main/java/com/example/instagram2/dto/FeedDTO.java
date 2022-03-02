package com.example.instagram2.dto;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class FeedDTO {

    private String myPicture;

    private List<ImagesAndTags> imagesAndTags;
}
