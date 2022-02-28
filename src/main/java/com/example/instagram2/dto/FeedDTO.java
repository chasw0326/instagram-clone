package com.example.instagram2.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FeedDTO {

    private String myPicture;

    private List<ImagesAndTags> imagesAndTags;
}
