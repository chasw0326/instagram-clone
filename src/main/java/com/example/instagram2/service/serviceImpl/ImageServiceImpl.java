package com.example.instagram2.service.serviceImpl;

import com.example.instagram2.dto.ImagesAndTags;
import com.example.instagram2.dto.ImgReply;
import com.example.instagram2.entity.Member;
import com.example.instagram2.entity.Reply;
import com.example.instagram2.repository.*;
import com.example.instagram2.service.ImageService;
import com.example.instagram2.dto.ImageReqDTO;
import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Tag;
import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.service.ReplyService;
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
    private final ReplyService replyService;

    @Value("${instagram.upload.path}")
    private String uploadPath;

    @Override
    @Transactional
    public Long uploadPicture(MultipartFile imgFile, ImageReqDTO imageDTO, AuthMemberDTO authMemberDTO) {

        String imageUrl = uploadService.uploadFile(imgFile, uploadPath);
        log.info("imageUrl: {}", imageUrl);
        Image image = dtoToEntity(imageDTO, imageUrl, authMemberDTO);
        List<Tag> tags = makeTagList(imageDTO.getTags(), image);
        image.setLikeCnt(0L);
        imageRepository.save(image);
        if (!tags.isEmpty()) {
            log.info("tags: {}", tags.toString());
            tagRepository.saveAll(tags);
        }
        return image.getIno();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImagesAndTags> getFeedImageData(Long userId, Pageable pageable) {
        Page<Image> images = imageRepository.getFollowFeed(userId, pageable);
        List<ImagesAndTags> feedDTOS = new ArrayList<>();
        images.forEach((image -> {
            Long ino = image.getIno();
            Long likeCnt = likesRepository.getLikesCntByImageId(ino);
            image.setLikeCnt(likeCnt);
            image.setLikeState(false);
            List<Long> mnoList = likesRepository.getMemberIdByImageId(ino);
            for (Long mno : mnoList) {
                if (mno.equals(userId)) {
                    image.setLikeState(true);
                    break;
                }
            }
            List<Tag> tags = tagRepository.findTagByImage_InoOrderByTno(ino);
            List<ImgReply> imgReplies = replyService.get3Replies(ino);
            feedDTOS.add(ImagesAndTags.builder()
                    .username(image.getMember().getUsername())
                    .memberId(image.getMember().getMno())
                    .tags(tags)
                    .replies(imgReplies)
                    .images(image)
                    .build());
        }));

        return feedDTOS;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Image> getPopularImageList(String username) {
        log.info("{}의 popualrImageList", username);
        Member member = memberRepository.getByUsername(username);
        Long userId = member.getMno();
        List<Image> images = imageRepository.get3PopularPictureList(userId);
        images.forEach((image -> {
            Long ino = image.getIno();
            Long likeCnt = likesRepository.getLikesCntByImageId(ino);
            image.setLikeCnt(likeCnt);
        }));
        return images;
    }

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
