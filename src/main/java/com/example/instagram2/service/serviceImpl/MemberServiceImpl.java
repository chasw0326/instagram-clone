package com.example.instagram2.service.serviceImpl;


import com.example.instagram2.dto.UserProfileRespDTO;
import com.example.instagram2.dto.UserUpdateReqDTO;
import com.example.instagram2.entity.Member;
import com.example.instagram2.repository.FollowRepository;
import com.example.instagram2.repository.ImageRepository;
import com.example.instagram2.repository.MemberRepository;
import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final ImageRepository imageRepository;
    private final UploadServiceImpl uploadService;


    @Value("${instagram.upload.path}")
    private String uploadPath;

    @Transactional
    public void changeProfilePicture(MultipartFile uploadFile, Long userId) {

        String fileName = uploadService.uploadFile(uploadFile, uploadPath);

        Optional<Member> result = memberRepository.findById(userId);
        if (result.isPresent()) {
            Member member = result.get();
            member.setProfileImageUrl(fileName);
            memberRepository.save(member);
        }
    }

    @Transactional
    public UserUpdateReqDTO getMemberInfo(Long userId) {
        Optional<Member> result = memberRepository.findById(userId);
        if (!result.isPresent()) {
            throw new RuntimeException("no user");
        }
        Member member = result.get();
        UserUpdateReqDTO dto = UserUpdateReqDTO.builder()
                .name(member.getName())
                .username(member.getUsername())
                .website(member.getWebsite())
                .intro(member.getIntro())
                .email(member.getEmail())
                .phone(member.getPhoneNum())
                .gender(member.getGender())
                .build();
        return dto;
    }

    @Transactional
    public void modifyMemberInfo(Long userId, UserUpdateReqDTO dto) {
        Optional<Member> result = memberRepository.findById(userId);
        if (result.isPresent()) {
            Member member = result.get();
            member.setName(dto.getName());
            member.setUsername(dto.getUsername());
            member.setWebsite(dto.getWebsite());
            member.setIntro(dto.getIntro());
            member.setEmail(dto.getEmail());
            member.setPhoneNum(dto.getPhone());
            member.setGender(dto.getGender());
            memberRepository.save(member);
        }
    }

    @Transactional(readOnly = true)
    public UserProfileRespDTO getUserProfile(String username, Long visitorId) throws IllegalArgumentException {
        UserProfileRespDTO userProfileRespDTO = new UserProfileRespDTO();
        Optional<Member> result = memberRepository.findByUsername(username);

        if (!result.isPresent()) {
            throw new IllegalArgumentException();
        }
        Member member = result.get();
        Long mno = member.getMno();
        int followState = followRepository.followState(visitorId, mno);
        Long followerCount = followRepository.getFollowerCount(mno);
        Long followCount = followRepository.getFollowCount(mno);
        Long imgCnt = imageRepository.getImageCount(mno);
        List<String> imageUrlList = imageRepository.getImageUrlList(mno);


        if (mno.equals(visitorId)) {
            userProfileRespDTO.setFollowState(false);
            userProfileRespDTO.setMyself(true);
        }else {
            userProfileRespDTO.setFollowState(followState == 1);
            userProfileRespDTO.setMyself(false);
        }
        userProfileRespDTO.setFollowCount(followCount);
        userProfileRespDTO.setFollowerCount(followerCount);
        userProfileRespDTO.setImageCount(imgCnt);
        userProfileRespDTO.setMember(member);
        userProfileRespDTO.setImgList(imageUrlList);


        return userProfileRespDTO;
    }

    @Override
    @Transactional
    public Long getMemberIdByUsername(String username) {
        Optional<Member> result = memberRepository.findByUsername(username);
        if (!result.isPresent()) {
            throw new RuntimeException("can't find member");
        }
        Member member = result.get();
        return member.getMno();
    }
}