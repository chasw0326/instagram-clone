package com.example.instagram2.serviceTests;


import com.example.instagram2.controller.ImageController;
import com.example.instagram2.dto.ImageReqDTO;
import com.example.instagram2.dto.ImagesAndTags;
import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Member;
import com.example.instagram2.entity.Tag;
import com.example.instagram2.repository.ImageRepository;
import com.example.instagram2.repository.MemberRepository;
import com.example.instagram2.repository.TagRepository;
import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;

import java.io.FileInputStream;
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

    @BeforeEach
    public void setUp() throws Exception {
        file = new MockMultipartFile("testImage",
                "testImg.png",
                "image/png",
                new FileInputStream("C:\\upload\\image_storage\\abc.png"));
    }

    @Test
    @WithUserDetails(value = "chasw@naver.com")
    void Should_UploadPicture() {

        ImageReqDTO dto = ImageReqDTO.builder()
                .caption("new Caption")
                .tags("#저글링 #히드라 #뮤탈리스크")
                .build();

        AuthMemberDTO loggedUser = (AuthMemberDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long ino = imageService.uploadPicture(file, dto, loggedUser);
        Optional<Image> result = imageRepository.findById(ino);
        List<Tag> tags = tagRepository.findTagByImage_InoOrderByTno(ino);
        Image image = result.get();
        System.out.println(image);
        assertEquals("new Caption", image.getCaption());
        assertEquals("저글링", tags.get(0).getName());
        assertEquals("히드라", tags.get(1).getName());
        assertEquals("뮤탈리스크", tags.get(2).getName());
    }


    @Test
    void Should_Get3PopularPicture(){
        String username = "96username";
        List<Image> images = imageService.getPopularImageList(username);
        System.out.println(images.toString());
        //2 107 151
        assertEquals(2,images.get(0).getIno());
        assertEquals(107,images.get(1).getIno());
        assertEquals(151,images.get(2).getIno());
    }


}

