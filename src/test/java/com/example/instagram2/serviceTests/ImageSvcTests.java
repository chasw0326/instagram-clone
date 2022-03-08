package com.example.instagram2.serviceTests;


import com.example.instagram2.controller.ImageController;
import com.example.instagram2.dto.ImageReqDTO;
import com.example.instagram2.dto.ImagesAndTags;
import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Member;
import com.example.instagram2.entity.Tag;
import com.example.instagram2.exception.myException.NoAuthorityException;
import com.example.instagram2.repository.ImageRepository;
import com.example.instagram2.repository.MemberRepository;
import com.example.instagram2.repository.TagRepository;
import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ImageSvcTests {

    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private TagRepository tagRepository;

    MockMultipartFile file;


    @Nested
    @DisplayName("uploadFile이 필요한 테스트들")
    class testsWithFile {

        @BeforeEach
        public void setUp() throws Exception {
            file = new MockMultipartFile("testImage",
                    "testImg.png",
                    "image/png",
                    new FileInputStream("C:\\upload\\image_storage\\abc.png"));
        }

        @DisplayName("정상적인 사진업로드")
        @Test
        @WithUserDetails(value = "chasw@naver.com")
        void Should_UploadPicture() throws IOException {

            ImageReqDTO dto = ImageReqDTO.builder()
                    .caption("new Caption")
                    .tags("#저글링 #히드라 #뮤탈리스크")
                    .build();

            AuthMemberDTO loggedUser = (AuthMemberDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long ino = imageService.uploadPicture(file, dto, loggedUser);
            Image image = imageRepository.findById(ino).orElse(null);
            List<Tag> tags = tagRepository.findTagByImage_InoOrderByTno(ino);
            System.out.println(image);

            assertEquals("new Caption", image.getCaption());
            assertEquals("저글링", tags.get(0).getName());
            assertEquals("히드라", tags.get(1).getName());
            assertEquals("뮤탈리스크", tags.get(2).getName());
        }

        @Test
        @WithUserDetails(value = "chasw@naver.com")
        void Should_delete() throws NoAuthorityException {
            AuthMemberDTO loggedUser = (AuthMemberDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            imageService.delete(208L, loggedUser.getId());
        }
    }

    @DisplayName("피드 이미지 가져오기")
    @Test
    void Should_GetFeedImageData(){
        PageRequest pageRequest = PageRequest.of(0,10, Sort.by("regDate").descending());
        List<ImagesAndTags> imagesAndTags = imageService.getFeedImageData(63L, pageRequest);
        for(ImagesAndTags iat : imagesAndTags){
            System.out.println(iat);
        }
        assertEquals(6, imagesAndTags.size());

        assertEquals(77L, imagesAndTags.get(0).getMemberId());
        assertEquals(185L, imagesAndTags.get(0).getImages().getIno());

        assertEquals(96L, imagesAndTags.get(1).getMemberId());
        assertEquals(151L, imagesAndTags.get(1).getImages().getIno());

        assertEquals(96L, imagesAndTags.get(2).getMemberId());
        assertEquals(107L, imagesAndTags.get(2).getImages().getIno());

        assertEquals(77L, imagesAndTags.get(3).getMemberId());
        assertEquals(75L, imagesAndTags.get(3).getImages().getIno());

        assertEquals(35L, imagesAndTags.get(4).getMemberId());
        assertEquals(58L, imagesAndTags.get(4).getImages().getIno());

        assertEquals(52L, imagesAndTags.get(5).getMemberId());
        assertEquals(5L, imagesAndTags.get(5).getImages().getIno());
    }


    @DisplayName("좋아요 많은순으로 사진3개 가져오기")
    @Test
    void Should_Get3PopularPicture() {
        String username = "87번이름";
        List<Image> images = imageService.getPopularImageList(username);
        System.out.println(images.toString());
        // 177 154 54
        assertEquals(3, images.size());
        assertEquals(177, images.get(0).getIno());
        assertEquals(154, images.get(1).getIno());
        assertEquals(54, images.get(2).getIno());
    }
}

