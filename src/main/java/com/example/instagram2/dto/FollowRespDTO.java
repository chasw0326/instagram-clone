package com.example.instagram2.dto;


import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class FollowRespDTO {

    private Long userId;

    private String username;

    private String profileImageUrl;

    private int followState;

    private int f4fState;
}
