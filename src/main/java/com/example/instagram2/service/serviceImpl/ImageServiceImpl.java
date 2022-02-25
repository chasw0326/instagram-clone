package com.example.instagram2.service.serviceImpl;

import com.example.instagram2.entity.Member;
import com.example.instagram2.repository.LikesRepository;
import com.example.instagram2.repository.MemberRepository;
import com.example.instagram2.service.ImageService;
import com.example.instagram2.dto.ImageReqDTO;
import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Tag;
import com.example.instagram2.repository.ImageRepository;
import com.example.instagram2.repository.TagRepository;
import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.service.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final TagRepository tagRepository;
    private final UploadService uploadService;
    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;

    @Value("${instagram.upload.path}")
    private String uploadPath;

    @Transactional
    @Override
    public Long uploadPicture(MultipartFile imgFile, ImageReqDTO imageDTO, AuthMemberDTO authMemberDTO) {

        String imageUrl = uploadService.uploadFile(imgFile, uploadPath);
        Image image = dtoToEntity(imageDTO, imageUrl,authMemberDTO);
        List<Tag> tags = makeTagList(imageDTO.getTags(), image);

        imageRepository.save(image);
        if(!tags.isEmpty()) {
            tagRepository.saveAll(tags);
        }
        return image.getIno();
    }

    @Transactional
    @Override
    public Page<Image> getFeedImage(Long userId, Pageable pageable) {
        Page<Image> images = imageRepository.getFollowFeed(userId, pageable);

        images.forEach((image -> {
            Long ino = image.getIno();
            Long likeCnt = likesRepository.getLikesCntByImageId(ino);
            image.setLikeCnt(likeCnt);
            List<Long> mnoList = likesRepository.getMemberIdByImageId(ino);
            for (Long mno : mnoList) {
                if (mno.equals(userId)) {
                    image.setLikeState(true);
                }
            }
        }));

        return images;
    }

    @Override
    public List<Image> getPopularImageList(String username) {

        Member member = memberRepository.getByUsername(username);
        Long userId = member.getMno();
        List<Image> images = imageRepository.getPopularPictureList(userId);
        return images;
    }

    // 나중에 따로 분리해서 private으로 해야함
    @Override
    public List<Tag> makeTagList(String tags, Image image) {
        String[] splitTag = tags.split("#");
        List<Tag> tagList = new ArrayList<>();

        for (int i = 1; i < splitTag.length; i++) {
            Tag tag = Tag.builder()
                    .name(splitTag[i].trim())
                    .image(image)
                    .build();

            tagList.add(tag);
        }
        return tagList;
    }

}
