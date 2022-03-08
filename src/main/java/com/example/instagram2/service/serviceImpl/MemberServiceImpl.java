package com.example.instagram2.service.serviceImpl;


import com.example.instagram2.dto.PasswordDTO;
import com.example.instagram2.dto.UserProfileRespDTO;
import com.example.instagram2.dto.UserEditDTO;
import com.example.instagram2.entity.Member;
import com.example.instagram2.exception.myException.DuplicationException;
import com.example.instagram2.exception.myException.InvalidFileException;
import com.example.instagram2.repository.FollowRepository;
import com.example.instagram2.repository.ImageRepository;
import com.example.instagram2.repository.MemberRepository;
import com.example.instagram2.service.MemberService;
import com.example.instagram2.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private final S3Uploader uploader;

//    @Value("${instagram.upload.path}")
//    private String uploadPath;

    @Override
    @Transactional
    public void changeProfilePicture(MultipartFile uploadFile, Long userId) throws IOException {

        String str = LocalDate.now().format(
                DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("/", File.separator);

        if (uploadFile == null) {
            log.error("uploadFile is null");
            throw new InvalidFileException("uploadFile is null");
        } else {
            String fileUrl = uploader.upload(uploadFile, folderPath);

            Optional<Member> result = memberRepository.findById(userId);
            if (result.isPresent()) {
                Member member = result.get();
                member.setProfileImageUrl(fileUrl);
                memberRepository.save(member);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = {IllegalArgumentException.class})
    public void deleteProfilePicture(Long userId) {
        log.info("delete profile Picture by userId: {}", userId);
        Optional<Member> result = memberRepository.findById(userId);
        if (result.isPresent()) {
            Member member = result.get();
            uploader.delete(member.getProfileImageUrl());
            member.setProfileImageUrl(null);
            memberRepository.save(member);
        } else {
            throw new IllegalArgumentException("no user by userId: " + userId);
        }
    }

    @Override
    @Transactional(rollbackFor = {IllegalArgumentException.class})
    public UserEditDTO getMemberInfo(Long userId) {
        Optional<Member> result = memberRepository.findById(userId);
        if (!result.isPresent()) {
            throw new IllegalArgumentException("no user by userId: " + userId);
        }
        Member member = result.get();
        log.info("get {}'s info", member.getUsername());
        UserEditDTO dto = entityToDto(member);
        return dto;
    }

    @Override
    @Transactional
    public PasswordDTO getProfileImgUrlAndUsernameById(Long userId) {
        Object[] arr = (Object[]) memberRepository.getProfileImagAndUsernameById(userId);
        String profileImg = (String) arr[0];
        String username = (String) arr[1];
        log.info("userId: {}'s profileImg: {}, username: {}", userId, profileImg, username);
        return PasswordDTO.builder()
                .mno(userId)
                .imgUrl(profileImg)
                .username(username)
                .build();
    }

    // 이메일은 readOnly로 해주세요
    @Override
    @Transactional(rollbackFor = DuplicationException.class)
    public void modifyMemberInfo(Long userId, UserEditDTO dto) {
        Optional<Member> result = memberRepository.findById(userId);
        if (result.isPresent()) {
            Member member = result.get();
            if (!member.getUsername().equals(dto.getUsername())) {
                if (memberRepository.existsByUsername(dto.getUsername())) {
                    log.warn("입력한 사용자 이름: {}", dto.getUsername());
                    throw new DuplicationException("중복된 사용자 이름 입니다.");
                }
            }
            dto.setMno(member.getMno());
            Member mem = dtoToEntity(dto);
            memberRepository.save(mem);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = {IllegalArgumentException.class})
    public UserProfileRespDTO getUserProfile(String username, Long visitorId) throws IllegalArgumentException {
        Optional<Member> result = memberRepository.findByUsername(username);

        if (!result.isPresent()) {
            throw new IllegalArgumentException("id에 맞는 멤버가 존재하지 않습니다.");
        }
        Member member = result.get();
        Long mno = member.getMno();
        int followState = followRepository.followState(visitorId, mno);
        Long followerCount = followRepository.getFollowerCount(mno);
        Long followCount = followRepository.getFollowCount(mno);
        Long imgCnt = imageRepository.getImageCount(mno);
        List<String> imageUrlList = imageRepository.getImageUrlList(mno);
        boolean dtoFollowState;
        boolean myself;

//        UserProfileRespDTO userProfileRespDTO = new UserProfileRespDTO();
        // 본인이 본인 사이트 들어갔을 경우
        if (mno.equals(visitorId)) {
            dtoFollowState = false;
            myself = true;
        } else {
            dtoFollowState = (followState == 1);
            myself = false;
        }
        UserProfileRespDTO dto = UserProfileRespDTO.builder()
                .followState(dtoFollowState)
                .myself(myself)
                .followCount(followCount)
                .followerCount(followerCount)
                .imageCount(imgCnt)
                .member(Member.builder()
                        .mno(mno)
                        .username(member.getUsername())
                        .name(member.getName())
                        .intro(member.getIntro())
                        .profileImageUrl(member.getProfileImageUrl())
                        .build())
                .imgUrlList(imageUrlList)
                .build();

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public String getProfileImg(Long userId) {
        return memberRepository.getProfileImageById(userId);
    }


    @Override
    @Transactional(readOnly = true, rollbackFor = {IllegalArgumentException.class})
    public Long getMemberIdByUsername(String username) {
        Optional<Member> result = memberRepository.findByUsername(username);
        if (!result.isPresent()) {
            throw new IllegalArgumentException("can't find username: " + username);
        }
        Member member = result.get();
        log.info(member.toString());
        return member.getMno();
    }
}