package com.example.instagram2.service;

import com.example.instagram2.dto.ImageReqDTO;
import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Member;
import com.example.instagram2.entity.Tag;
import com.example.instagram2.security.dto.AuthMemberDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ImageService {

    Long uploadPicture(ImageReqDTO imageDTO, AuthMemberDTO authMemberDTO);

    Page<Image> getFeedImage(Long userId, Pageable pageable);

    List<Tag> makeTagList(String tags, Image image);

    List<Image> getPopularImageList(Long userId);

    default Image dtoToEntity(ImageReqDTO imageDTO, AuthMemberDTO authMemberDTO){
        return Image.builder()
                .caption(imageDTO.getCaption())
                .ImageUrl(imageDTO.getImageUrl())
                .member(Member.builder()
                        .mno(authMemberDTO.getId())
                        .email(authMemberDTO.getEmail())
                        .build())
                .build();
    }
}
