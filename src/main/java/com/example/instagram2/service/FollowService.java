package com.example.instagram2.service;


import com.example.instagram2.dto.FollowRespDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FollowService {

    Page<FollowRespDTO> getFollowList(Long visitorId, Long pageMemberId,
                                             Pageable pageable);
    Page<FollowRespDTO> getFollowerList(Long visitorId, Long pageMemberId, Pageable pageable);

    void follow(Long fromMemberId, Long toMemberId);

    void unFollow(Long fromMemberId, Long toMemberId);

//    default FollowRespDTO dtoToEntity(Follow follow) {
//
//
//
//    }
}
