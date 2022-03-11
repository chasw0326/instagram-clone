package com.example.instagram2.service.serviceImpl;

import com.example.instagram2.dto.ImagesAndTags;
import com.example.instagram2.dto.ImgReply;
import com.example.instagram2.entity.Member;
import com.example.instagram2.exception.myException.NoAuthorityException;
import com.example.instagram2.repository.*;
import com.example.instagram2.service.ImageService;
import com.example.instagram2.dto.ImageReqDTO;
import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Tag;
import com.example.instagram2.security.dto.AuthMemberDTO;
import com.example.instagram2.service.ReplyService;
import com.example.instagram2.service.UploadService;
import com.example.instagram2.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * <code>ImageService</code><br>
 * 이미지(글)에 관련된 서비스
 *
 * @author chasw326
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class ImageServiceImpl implements ImageService {


    /**
     * 현재는 로컬에 저장하게 코드가 구성되어 있고, <br>
     * <Strong>S3Uploader</Strong>를 사용하고 싶으면 아래 주석들을 풀어주셔야 합니다. <br>
     * S3을 사용하기 위한건 'S3' 주석으로 <br>
     * 로컬을 사용하기 위한건 'local' 주석으로 표기하겠습니다.
     *
     * @see com.example.instagram2.util.S3Uploader
     */
    private final ImageRepository imageRepository;
    private final TagRepository tagRepository;

    // local
    private final UploadService uploadService;
    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;
    private final ReplyService replyService;

    // s3
//    private final S3Uploader uploader;


    /**
     * 사진 업로드<br>
     * S3을 사용하기 위해선 메서드 기준 위에서 5줄 주석을 풀고<br>
     * 그 아래줄을 주석 처리 하셔야 합니다.
     * @param imgFile (사진 파일)
     * @param imageDTO (캡션, 태그)
     * @param authMemberDTO (인증받은 유저)
     * @return (이미지 pk)
     * @throws IOException
     */
    @Override
    @Transactional
    public Long uploadPicture(MultipartFile imgFile, ImageReqDTO imageDTO, AuthMemberDTO authMemberDTO) throws IOException {


//        String str = LocalDate.now().format(
//                DateTimeFormatter.ofPattern("yyyy/MM/dd"));
//
//        String folderPath = str.replace("/", File.separator);
//        String imageUrl = uploader.upload(imgFile, folderPath);
        /**
         * local
         */
        //    @Value("${instagram.upload.path}")
        String uploadPath = "C:\\upload\\image_storage";
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


    /**
     * 피드데이터를 가져온다.
     * 좋아요를 두번 누를수 없게, likeState를 통해<br>
     * 내가 좋아요를 눌렀는지 안 눌렀는지 확인한다. <br>
     * 좋아요는 @Transient임으로 likes테이블에서 개수를 가져와서 set해준다.
     * @param userId (현재 로그인한 회원)
     * @param pageable
     * @return 댓글들과 글의정보들(좋아요, 태그 등)
     */
    @Override
    @Transactional(readOnly = true)
    public List<ImagesAndTags> getFeedImageData(Long userId, Pageable pageable) {
        Page<Image> images = imageRepository.getFollowFeed(userId, pageable);
        List<ImagesAndTags> feedDTOS = new ArrayList<>();
        images.forEach((image -> {
            Long ino = image.getIno();
            Long likeCnt = likesRepository.getLikesCntByImageId(ino);
            List<Long> mnoList = likesRepository.getMemberIdByImageId(ino);
            image.setLikeCnt(likeCnt);
            // likeState 디폴트값이 null임으로 false로 설정하고
            // 좋아요 누른 사람중에서 본인pk가 있으면 true로 설정하고 break한다.
            image.setLikeState(false);
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

    /**
     * 좋아요 많이 받은 순으로 이미지 3개 가져온다.
     * Image에 포함된 Member는 Jackson으로 json에서 제외한다.
     * @param username (사용자 이름)
     * @return imageList
     */
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

    /**
     * 이미지(글 삭제)
     * @param ino (이미지 pk)
     * @param principalId (인증받은 유저)
     * @throws NoAuthorityException (다른사람이 삭제시도 했을때)
     */
    @Override
    @Transactional
    public void delete(Long ino, Long principalId) throws NoAuthorityException {
        Optional<Image> result = imageRepository.findById(ino);
        if (!result.isPresent()) {
            return;
        }
        Image image = result.get();
        if (image.getMember().getMno().equals(principalId)) {
             //S3Uploader 쓰려면 주석 풀어야함 S3 삭제 기능
//            uploader.delete(image.getImageUrl());
            imageRepository.delete(image);
        } else {
            throw new NoAuthorityException("권한이 없습니다.");
        }

    }

    /**
     * 문자열로 태그들이 오기 때문에<br>
     * '#'으로 split 해서 tagList를 만들어줌
     * @param tags (문자열로 태그들 보냄)
     * @param image (태그가 붙은 이미지들)
     * @return tagList 태그리스트
     */
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
