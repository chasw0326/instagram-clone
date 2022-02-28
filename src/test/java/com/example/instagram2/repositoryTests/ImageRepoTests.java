package com.example.instagram2.repositoryTests;


import com.example.instagram2.entity.Image;
import com.example.instagram2.repository.ImageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.LongStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ImageRepoTests {

    @Autowired
    private ImageRepository repository;

    @Test
    public void followFeedTest() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Image> images = repository.getFollowFeed(1L, pageRequest).getContent();
        System.out.println("|------------피드테스트------------|");
        for (Image img : images)
            System.out.println(img);
        System.out.println("|--------------------------------|");
    }

    @Test
    public void getMemberImageWithLikeCntAndReplyCountTest(){
        List<String> result = repository.getImageUrlList(100L);

        for(String str : result){
            System.out.println(str);
        }
    }

    @Test
    public void getImgUrlListTest(){
        List<String> result = repository.getImageUrlList(2L);
        System.out.println(result);
    }

    @Test
    public void getPopularImageTest() {
        List<Image> popularImgList = repository.get3PopularPictureList(96L);

        for (Image img : popularImgList) {
            System.out.println(img);
        }
    }

    @Test
    public void getImageCountTest(){
        Long imgCnt = repository.getImageCount(2L);
        assertEquals(3L, imgCnt);
    }

    @Test
    public void uselessTest() {
        LongStream.rangeClosed(1, 90).forEach(i -> {
            Long rndLikeCnt = (long) (Math.random() * 100) + 1;
            Image image = Image.builder()
                    .ino(i)
                    .likeCnt(rndLikeCnt)
                    .build();
            repository.save(image);
        });
    }
}
