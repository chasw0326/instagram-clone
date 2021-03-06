package com.example.instagram2.service;

import com.example.instagram2.dto.ImagesAndTags;
import com.example.instagram2.dto.ImageReqDTO;
import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Member;
import com.example.instagram2.entity.Tag;
import com.example.instagram2.exception.myException.NoAuthorityException;
import com.example.instagram2.security.dto.AuthMemberDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**<strong>구현클래스에 문서화</strong><br>
 * {@link com.example.instagram2.service.serviceImpl.ImageServiceImpl}
 */
public interface ImageService {

    Long uploadPicture(MultipartFile imgFile, ImageReqDTO imageDTO, AuthMemberDTO authMemberDTO) throws IOException;

    List<ImagesAndTags> getFeedImageData(Long userId, Pageable pageable);

    void delete(Long ino, Long principalId) throws NoAuthorityException;

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
