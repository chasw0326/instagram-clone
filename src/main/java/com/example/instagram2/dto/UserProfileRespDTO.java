package com.example.instagram2.dto;


import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Member;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserProfileRespDTO {

    private boolean followState;

    private Long followCount;

    private Long followerCount;

    private Long imageCount;

    private Member member;

    private List<String> imgUrlList;

    private boolean myself; //본인인지 확인
}
