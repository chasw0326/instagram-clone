package com.example.instagram2.dto;


import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageReqDTO {

    @NotNull(message = "이미지 파일은 필수 입니다.")
    private MultipartFile uploadFile;

    private String caption;

    private String tags;

    private String imageUrl;
}
