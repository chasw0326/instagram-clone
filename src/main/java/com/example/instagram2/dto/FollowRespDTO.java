package com.example.instagram2.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FollowRespDTO {

    private Long userId;

    private String username;

    private String profileImageUrl;

    private int followState;

    private int f4fState;
}
