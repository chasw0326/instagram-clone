package com.example.instagram2.controller;

import com.example.instagram2.dto.FeedDTO;
import com.example.instagram2.dto.ImagesAndTags;
import com.example.instagram2.dto.ImageReqDTO;
import com.example.instagram2.entity.Image;
import com.example.instagram2.exception.ArgumentCheckUtil;
import com.example.instagram2.exception.myException.NoAuthorityException;
import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.service.ImageService;
import com.example.instagram2.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * <code>AuthController</code><br>
 * 회원가입, 비밀번호 변경등 중요한 정보들 처리
 * @author chasw326
 */
@Api(tags = "이미지 API")
@RestController
@Log4j2
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final MemberService memberService;
    private final ArgumentCheckUtil argumentCheckUtil;

    @ApiOperation(value = "피드 이미지 가져오기")
    @GetMapping({"/image"})
    public ResponseEntity<?> getFeedImages(@AuthenticationPrincipal AuthMemberDTO authMember,
                                           @PageableDefault(
                                                   size = 15,
                                                   sort = "regDate",
                                                   direction = Sort.Direction.DESC) Pageable pageable) {

        String userProfileImg = memberService.getProfileImg(authMember.getId());
        List<ImagesAndTags> imagesAndTags = imageService.getFeedImageData(authMember.getId(), pageable);
        FeedDTO feedDTOS = FeedDTO.builder()
                .myPicture(userProfileImg)
                .imagesAndTags(imagesAndTags)
                .build();

        return ResponseEntity.ok().body(feedDTOS);
    }

    @ApiOperation(value = "글 업로드 with 이미지")
    @PostMapping(value = "/image/create/style",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> upload(@ApiParam(value = "올릴 파일")@RequestPart MultipartFile imgFile,
                                    @ApiParam(value = "설명과 태그")@RequestPart @Valid ImageReqDTO imageReqDTO,
                                    @AuthenticationPrincipal AuthMemberDTO authMember) throws IOException {
        log.info("upload");
        Long ino = imageService.uploadPicture(imgFile, imageReqDTO, authMember);
        return ResponseEntity.ok().body("ImageId: " + ino);

    }


    @ApiOperation(value = "인기 이미지 3개 가져오기")
    @GetMapping("/image/explore/{username}")
    public ResponseEntity<?> getPopularPicture(@ApiParam(value = "사용자이름")@PathVariable String username) {
        log.info("getPopularPicture");
        argumentCheckUtil.existByUsername(username);
        List<Image> images = imageService.getPopularImageList(username);
        return ResponseEntity.ok().body(images);
    }

    @ApiOperation(value = "이미지 삭제")
    @DeleteMapping("/image/")
    public ResponseEntity<?> deletePicture(@ApiParam(value = "이미지 id")Long ino, @AuthenticationPrincipal AuthMemberDTO authMemberDTO) throws NoAuthorityException {
        imageService.delete(ino, authMemberDTO.getId());
        return ResponseEntity.ok().body("delete");
    }
}

