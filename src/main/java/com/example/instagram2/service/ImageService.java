package com.example.instagram2.service;

import com.example.instagram2.dto.ImagesAndTags;
import com.example.instagram2.dto.ImageReqDTO;
import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Member;
import com.example.instagram2.entity.Tag;
import com.example.instagram2.security.dto.AuthMemberDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    Long uploadPicture(MultipartFile imgFile, ImageReqDTO imageDTO, AuthMemberDTO authMemberDTO);

    List<ImagesAndTags> getFeedImageData(Long userId, Pageable pageable);

    List<Tag> makeTagList(String tags, Image image);

    List<Image> getPopularImageList(String username);

    default Image dtoToEntity(ImageReqDTO imageDTO,String imageUrl, AuthMemberDTO authMemberDTO){
        return Image.builder()
                .caption(imageDTO.getCaption())
                .ImageUrl(imageUrl)
                .member(Member.builder()
                        .mno(authMemberDTO.getId())
                        .email(authMemberDTO.getEmail())
                        .build())
                .build();
    }
}
