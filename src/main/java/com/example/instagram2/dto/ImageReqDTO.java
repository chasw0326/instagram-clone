package com.example.instagram2.dto;


import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Member;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class ImageReqDTO {

    private MultipartFile uploadFile;

    private String caption;

    private String tags;

    private String imageUrl;
}
