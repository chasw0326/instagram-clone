package com.example.instagram2.service.serviceImpl;

import com.example.instagram2.repository.MemberRepository;
import com.example.instagram2.service.FollowService;
import com.example.instagram2.dto.FollowRespDTO;
import com.example.instagram2.entity.Follow;
import com.example.instagram2.entity.Member;
import com.example.instagram2.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
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
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public List<FollowRespDTO> getFollowerList(Long visitorId,
                                               String pageMemberName,
                                               Pageable pageable) {
        log.info("VisitorId: {}", visitorId);
        log.info("PageMemberId: {}", pageMemberName);

        Page<Object[]> followerData = followRepository.getFollowerData(pageMemberName, pageable);
        List<Object[]> result = followerData.getContent();

        return getEachFollowState(visitorId, result);

    }

    @Override
    @Transactional(readOnly = true)
    public List<FollowRespDTO> getFollowList(Long visitorId,
                                             String pageMemberName,
                                             Pageable pageable) {
        log.info("VisitorId: {}", visitorId);
        log.info("PageMemberId: {}", pageMemberName);
        Page<Object[]> followData = followRepository.getFollowData(pageMemberName, pageable);
        List<Object[]> result = followData.getContent();
        return getEachFollowState(visitorId, result);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FollowRespDTO> getEachFollowState(Long visitorId, List<Object[]> followData) {

        log.info("VisitorId: {}", visitorId);

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

        return result;
    }


    @Override
    @Transactional
    public void follow(Long fromMemberId, Long toMemberId) {
        log.info("{} follow {}", fromMemberId, toMemberId);
        if (!fromMemberId.equals(toMemberId)) {
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
    }

    @Override
    @Transactional
    public void unFollow(Long fromMemberId, Long toMemberId) {
        log.info("{} unfollow {}", fromMemberId, toMemberId);
        followRepository.unFollow(fromMemberId, toMemberId);
    }
}
