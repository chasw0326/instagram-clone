package com.example.instagram2.service;


import com.example.instagram2.dto.PasswordDTO;
import com.example.instagram2.dto.UserProfileRespDTO;
import com.example.instagram2.dto.UserEditDTO;
import com.example.instagram2.entity.Member;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MemberService {

    void changeProfilePicture(MultipartFile uploadFile, Long userId) throws IOException;

    UserProfileRespDTO getUserProfile(String username, Long visitorId) throws IllegalArgumentException;

    UserEditDTO getMemberInfo(Long userId);

    void deleteProfilePicture(Long userId);

    PasswordDTO getProfileImgUrlAndUsernameById(Long userId);

    String getProfileImg(Long mno);

    void modifyMemberInfo(Long userId, UserEditDTO dto);

    Long getMemberIdByUsername(String username);


    default Member dtoToEntity(UserEditDTO dto) {
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

    default UserEditDTO entityToDto(Member member) {
        return UserEditDTO.builder()
                .name(member.getName())
                .username(member.getUsername())
                .website(member.getWebsite())
                .intro(member.getIntro())
                .email(member.getEmail())
                .phone(member.getPhoneNum())
                .gender(member.getGender())
                .build();
    }
}
