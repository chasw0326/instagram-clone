package com.example.instagram2.controller;


import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.exception.ArgumentCheckUtil;
import com.example.instagram2.service.FollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;

/**
 * <code>FollowController</code><br>
 * 팔로우에 대한 컨트롤러
 * @author chasw326
 */
@Api(tags = "팔로우 컨트롤러")
@RestController
@Log4j2
@RequestMapping("/follow/")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final ArgumentCheckUtil argumentCheckUtil;

    /**
     * 팔로우할 멤버가 존재하는지 확인하고, <br>
     * 팔로우를 합니다.
     * @param authMember
     * @param toMemberId
     * @return 200 or 400 <br>
     * 400은 exceptionHandler에서 보냅니다.
     */
    @ApiOperation(value = "팔로우")
    @PostMapping("{toMemberId}")
    public ResponseEntity<?> follow(@AuthenticationPrincipal AuthMemberDTO authMember,
                                    @ApiParam(value = "팔로우할 사람")@PathVariable Long toMemberId) {
        log.info("follow");
        argumentCheckUtil.existByMemberId(toMemberId);
        followService.follow(authMember.getId(), toMemberId);
        return ResponseEntity.ok().body("follow");
    }

    /**
     * 언팔로우할 멤버가 존재하는지 확인하고, <br>
     * 언팔로우를 합니다.
     * @param authMember
     * @param toMemberId
     * @return 200 or 400 <br>
     * 400은 exceptionHandler에서 보냅니다.
     */
    @ApiOperation(value = "언팔로우")
    @DeleteMapping("{toMemberId}")
    public ResponseEntity<?> unfollow(@AuthenticationPrincipal AuthMemberDTO authMember,
                                      @ApiParam(value = "언팔로우할 사람")@PathVariable Long toMemberId){

        log.info("unfollow");
        argumentCheckUtil.existByMemberId(toMemberId);
        followService.unFollow(authMember.getId(), toMemberId);
        return ResponseEntity.ok().body("unfollow");
    }
}
