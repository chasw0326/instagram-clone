package com.example.instagram2.dto;

import com.example.instagram2.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FeedDTO {

    String profileImgUrl;

    Page<Image> images;

}
