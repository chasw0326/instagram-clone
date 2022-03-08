package com.example.instagram2.dto;


import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

@ApiModel(value="에러 DTO", description = "에러 정보 보내줌")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResponseDTO<T> {

    private String error;
    private List<T> data;

}
