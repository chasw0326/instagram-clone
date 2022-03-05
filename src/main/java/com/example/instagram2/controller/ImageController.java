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
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final MemberService memberService;
    private final ArgumentCheckUtil argumentCheckUtil;

    @GetMapping({"/", "/image"})
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

    @PostMapping(value = "/image/create/style",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> upload(@RequestPart MultipartFile imgFile,
                                    @RequestPart @Valid ImageReqDTO imageReqDTO,
                                    @AuthenticationPrincipal AuthMemberDTO authMember) {
        log.info("upload");
        Long ino = imageService.uploadPicture(imgFile, imageReqDTO, authMember);
        return ResponseEntity.ok().body("ImageId: " + ino);

    }


    @GetMapping("/image/explore/{username}")
    public ResponseEntity<?> getPopularPicture(@PathVariable String username) {
        log.info("getPopularPicture");
        argumentCheckUtil.existByUsername(username);
        List<Image> images = imageService.getPopularImageList(username);
        return ResponseEntity.ok().body(images);
    }

    @DeleteMapping("/image/")
    public ResponseEntity<?> deletePicture(Long ino, @AuthenticationPrincipal AuthMemberDTO authMemberDTO) throws NoAuthorityException {
        imageService.delete(ino, authMemberDTO.getId());
        return ResponseEntity.ok().body("delete");
    }
}

