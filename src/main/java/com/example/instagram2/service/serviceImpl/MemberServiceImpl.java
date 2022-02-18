package com.example.instagram2.service.serviceImpl;


import com.example.instagram2.dto.PasswordDTO;
import com.example.instagram2.dto.UserProfileRespDTO;
import com.example.instagram2.dto.UserEditDTO;
import com.example.instagram2.entity.Member;
import com.example.instagram2.exception.myException.IllegalFileException;
import com.example.instagram2.repository.FollowRepository;
import com.example.instagram2.repository.ImageRepository;
import com.example.instagram2.repository.MemberRepository;
import com.example.instagram2.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
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

        if(uploadFile == null){
            throw new IllegalFileException("uploadFile is null");
        }
        String fileName = uploadService.uploadFile(uploadFile, uploadPath);

        Optional<Member> result = memberRepository.findById(userId);
        if (result.isPresent()) {
            Member member = result.get();
            member.setProfileImageUrl(fileName);
            memberRepository.save(member);
        }
    }

    @Transactional
    public UserEditDTO getMemberInfo(Long userId) {
        Optional<Member> result = memberRepository.findById(userId);
        if (!result.isPresent()) {
            throw new RuntimeException("no user");
        }
        Member member = result.get();
        UserEditDTO dto = entityToDto(member);
        return dto;
    }

    @Transactional
    public PasswordDTO getProfileImgUrlAndUsernameById(Long userId) {
        Object[] arr = (Object[]) memberRepository.getProfileImagAndUsernameById(userId);
        String profileImg = (String) arr[0];
        String username = (String) arr[1];
        return PasswordDTO.builder()
                .mno(userId)
                .imgUrl(profileImg)
                .username(username)
                .build();
    }

    @Transactional
    public void modifyMemberInfo(Long userId, UserEditDTO dto) {
        Optional<Member> result = memberRepository.findById(userId);
        if (result.isPresent()) {
            Member member = result.get();
            dto.setMno(member.getMno());
            Member mem = dtoToEntity(dto);
            memberRepository.save(mem);
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
        } else {
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
    public String getProfileImg(Long mno){
        return memberRepository.getProfileImageById(mno);
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