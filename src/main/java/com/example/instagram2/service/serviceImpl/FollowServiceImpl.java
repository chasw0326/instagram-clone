package com.example.instagram2.service.serviceImpl;

import com.example.instagram2.service.FollowService;
import com.example.instagram2.dto.FollowRespDTO;
import com.example.instagram2.entity.Follow;
import com.example.instagram2.entity.Member;
import com.example.instagram2.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<FollowRespDTO> getFollowerList(Long visitorId, Long pageMemberId, Pageable pageable) {
        Page<Object[]> followerData = followRepository.getFollowerData(pageMemberId);
        return getEachFollowState(visitorId, pageable, followerData);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FollowRespDTO> getFollowList(Long visitorId, Long pageMemberId,
                                             Pageable pageable) {
        Page<Object[]> followData = followRepository.getFollowData(pageMemberId);
        return getEachFollowState(visitorId, pageable, followData);
    }

    private Page<FollowRespDTO> getEachFollowState(Long visitorId, Pageable pageable, Page<Object[]> followData) {
        List<FollowRespDTO> result = new ArrayList<>();

        followData.forEach((data -> {
            Long userId = (Long) data[0];
            String username = (String) data[1];
            String profileImageUrl = (String) data[2];
            int followState = followRepository.followState(visitorId, userId);
            int f4fState = followRepository.followState(userId, visitorId);

            FollowRespDTO dto = FollowRespDTO.builder()
                    .userId(userId)
                    .username(username)
                    .profileImageUrl(profileImageUrl)
                    .followState(followState)
                    .f4fState(f4fState)
                    .build();

            result.add(dto);
        }));

        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), result.size());
        final Page<FollowRespDTO> page = new PageImpl<>(result.subList(start, end), pageable, result.size());
        return page;
    }


    @Override
    @Transactional
    public void follow(Long fromMemberId, Long toMemberId) {
        Member fromMember = Member.builder()
                .mno(fromMemberId)
                .build();
        Member toMember = Member.builder()
                .mno(toMemberId)
                .build();
        Follow follow = Follow.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .build();

        followRepository.save(follow);
    }

    @Override
    @Transactional
    public void unFollow(Long fromMemberId, Long toMemberId) {
        followRepository.unFollow(fromMemberId, toMemberId);
    }
}
