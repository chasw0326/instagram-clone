package com.example.instagram2.service;


import com.example.instagram2.dto.UserProfileRespDTO;
import com.example.instagram2.dto.UserUpdateReqDTO;
import com.example.instagram2.entity.Member;
import com.example.instagram2.security.dto.AuthMemberDTO;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {

    void changeProfilePicture(MultipartFile uploadFile, Long userId);

    UserProfileRespDTO getUserProfile(String username, Long visitorId) throws IllegalArgumentException;

    UserUpdateReqDTO getMemberInfo(Long userId);

    void modifyMemberInfo(Long userId, UserUpdateReqDTO dto);

    Long getMemberIdByUsername(String username);

    default Member dtoToEntity(UserUpdateReqDTO dto) {
        return Member.builder()
                .mno(dto.getMno())
                .name(dto.getName())
                .username(dto.getUsername())
                .website(dto.getWebsite())
                .intro(dto.getIntro())
                .email(dto.getEmail())
                .phoneNum(dto.getPhone())
                .gender(dto.getGender())
                .build();
    }
}
