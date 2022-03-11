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
import com.example.instagram2.service.UploadService;
import com.example.instagram2.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * <code>MemberService</code><br>
 * 멤버에 관련된 서비스
 *
 * @author chasw326
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    /**
     * 현재는 로컬에 저장하게 코드가 구성되어 있고, <br>
     * <Strong>S3Uploader</Strong>를 사용하고 싶으면 아래 주석들을 풀어주셔야 합니다. <br>
     * S3을 사용하기 위한건 'S3' 주석으로 <br>
     * 로컬을 사용하기 위한건 'local' 주석으로 표기하겠습니다.
     *
     * @see com.example.instagram2.util.S3Uploader
     */
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final ImageRepository imageRepository;
    private final UploadService uploadService;
    // s3
//    private final S3Uploader uploader;

    /**
     * 프로필 사진 변경<br>
     * 현재는 로컬에 저장하는 방식이고 S3을 사용하기 위해선 <br>
     * S3주석을 푸시고 local에 저장하는 코드를 주석처리 해주셔야 합니다.
     * @param uploadFile (업로드 파일)
     * @param userId (프로필 사진 변경할 유저id)
     * @throws IOException
     */
    @Override
    @Transactional
    public void changeProfilePicture(MultipartFile uploadFile, Long userId) throws IOException {
// -------------------------------- s3 ---------------------------------------
//        String str = LocalDate.now().format(
//                DateTimeFormatter.ofPattern("yyyy/MM/dd"));
//
//        String folderPath = str.replace("/", File.separator);
// -------------------------------- s3 ---------------------------------------
        if (uploadFile == null) {
            log.error("uploadFile is null");
            throw new InvalidFileException("uploadFile is null");
        } else {
            // -------------------------------- s3 ---------------------------------------
//            String fileUrl = uploader.upload(uploadFile, folderPath);
            // local
            //    @Value("${instagram.upload.path}")
            String uploadPath = "C:\\upload\\image_storage";
            String fileUrl = uploadService.uploadFile(uploadFile, uploadPath);
            // -------------------------------- s3 ---------------------------------------
            Optional<Member> result = memberRepository.findById(userId);
            if (result.isPresent()) {
                Member member = result.get();
                member.setProfileImageUrl(fileUrl);
                memberRepository.save(member);
            }
        }
    }

    /**
     * 프로필사진 삭제
     * @param userId
     * S3을 사용하기 위해선 s3 주석을 풀어주세요
     */
    @Override
    @Transactional(rollbackFor = {IllegalArgumentException.class})
    public void deleteProfilePicture(Long userId) {
        log.info("delete profile Picture by userId: {}", userId);
        Optional<Member> result = memberRepository.findById(userId);
        if (result.isPresent()) {
            Member member = result.get();

//            uploader.delete(member.getProfileImageUrl());         // s3
            member.setProfileImageUrl(null);
            memberRepository.save(member);
        } else {
            throw new IllegalArgumentException("no user by userId: " + userId);
        }
    }

    /**
     * 유저info 가져오기
     * @param userId
     * @return
     * @see com.example.instagram2.dto.UserEditDTO
     */
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

    /**
     * 비밀번호 변경 GET으로 요청하면<br>
     * 사용자 이름과 프로필사진을 보내준다.
     * @param userId
     * @return
     */
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

    /**
     * 유저정보 수정<br>
     * <strong>이메일은 ReadOnly로 처리해 주세요</strong><br>
     * 사용자이름을 변경한다면 중복체크를 합니다. (바꾸지 않으면 하지 않음)
     * @param userId
     * @param dto
     * @see com.example.instagram2.dto.UserEditDTO
     */
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

    /**
     * 방문하는 페이지 멤버의 정보를 가져온다.<br>
     * 나랑 팔로우했는지 누굴 팔로우 했는지 누가 팔로우 했는지<br>
     * 본인이 본인 페이지 들어갔는지 이미지갯수는 몇개인지 이미지리스트 url 등등
     * @param username (페이지 멤버)
     * @param visitorId (방문자)
     * @return
     * @see com.example.instagram2.dto.UserProfileRespDTO
     * @throws IllegalArgumentException
     */
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

    /**
     * 프로필 이미지 가져오기
     * @param userId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public String getProfileImg(Long userId) {
        return memberRepository.getProfileImageById(userId);
    }


    /**
     * 유저네임으로 멤버 아이디 가져오기
     * @param username
     * @return 멤버id
     */
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