package com.example.instagram2.dto;


import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileRespDTO {

    private boolean followState;

    private Long followCount;

    private Long followerCount;

    private Long imageCount;

    private Member member;

    private List<String> imgList;

    private boolean myself; //본인인지 확인
}
