package com.example.instagram2.dto;


import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Member;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel(value="ImageReqDTO", description = "이미지 올리면서 같이 보낼 캡션과 태그들")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageReqDTO implements Serializable {

    @NotNull(message = "caption is null")
    private String caption;

    private String tags;
}
