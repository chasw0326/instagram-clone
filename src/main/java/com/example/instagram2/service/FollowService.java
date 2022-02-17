package com.example.instagram2.service;


import com.example.instagram2.dto.FollowRespDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FollowService {

    List<FollowRespDTO> getFollowList(Long visitorId,
                                      String pageMemberName,
                                      Pageable pageable);
    List<FollowRespDTO> getFollowerList(Long visitorId,
                                        String pageMemberName,
                                        Pageable pageable);

    List<FollowRespDTO> getEachFollowState(Long visitorId, List<Object[]> followData);

    void follow(Long fromMemberId, Long toMemberId);

    void unFollow(Long fromMemberId, Long toMemberId);

}
