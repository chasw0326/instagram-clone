package com.example.instagram2.serviceTests;


import com.example.instagram2.dto.ImageReqDTO;
import com.example.instagram2.entity.Member;
import com.example.instagram2.repository.MemberRepository;
import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.FileInputStream;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
public class ImageSvcTests {

    @Autowired
    private ImageService imageService;

    @Autowired
    private MemberRepository memberRepository;

    MockMultipartFile file;

    @BeforeEach
    public void setUp() throws Exception {
        file = new MockMultipartFile("testImage",
                "testImg.png",
                "image/png",
                new FileInputStream("C:\\upload\\image_storage\\abc.png"));
    }

    @Test
    public void uploadFileTest() {

        String caption = "테스트용 캡션";
        String tags = "#태그1 #태그2 #태그3";

        ImageReqDTO imageDto = ImageReqDTO.builder()
                .uploadFile(file)
                .caption(caption)
                .tags(tags)
                .build();

        Optional<Member> result = memberRepository.findByEmailAndSocial("test10@example.com", false);

        Member member = result.get();

        AuthMemberDTO authMember = new AuthMemberDTO(
                member.getEmail(),
                member.getPassword(),
                member.isFromSocial(),
                member.getRoleSet().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toSet())
                , member.getMno());

        imageService.uploadPicture(imageDto, authMember);
    }


}
