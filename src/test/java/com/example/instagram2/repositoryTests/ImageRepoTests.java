package com.example.instagram2.repositoryTests;


import com.example.instagram2.entity.Image;
import com.example.instagram2.repository.ImageRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ImageRepoTests {

    @Autowired
    private ImageRepository repository;

    @Disabled
    @DisplayName("getFollowFeed")
    @Test
    public void followFeedTest() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Image> images = repository.getFollowFeed(1L, pageRequest).getContent();
        System.out.println("|------------피드테스트------------|");
        for (Image img : images)
            System.out.println(img);
        System.out.println("|--------------------------------|");
    }

    @Disabled
    @DisplayName("get3PopularPictureList")
    @Test
    public void get3PopularPictureTest(){
        List<Image> images = repository.get3PopularPictureList(11L);
        for(Image img : images){
            System.out.println(img);
        }
    }

    @Disabled
    @DisplayName("getImageUrlList")
    @Test
    public void getImgUrlListTest(){
        List<String> result = repository.getImageUrlList(2L);
        System.out.println(result.toString());
    }

    @Disabled
    @DisplayName("get3PopularPictureList")
    @Test
    public void getPopularImageTest() {
        List<Image> popularImgList = repository.get3PopularPictureList(96L);

        for (Image img : popularImgList) {
            System.out.println(img);
        }
    }

    @Disabled
    @DisplayName("getImageCount")
    @Test
    public void getImageCountTest(){
        Long imgCnt = repository.getImageCount(2L);
        assertEquals(3L, imgCnt);
    }

    @Disabled
    @DisplayName("existsByIno")
    @Test
    void verifyExistsByIno() {
        assertFalse(repository.existsByIno(2480234828978L));
        assertTrue(repository.existsByIno(191L));
    }

    @Disabled
    @DisplayName("existsByIno")
    @Test
    void verifyFindTagsSearch() {
        System.out.println(repository.findTagsSearch("저글링"));
        assertEquals(3, repository.findTagsSearch("저글링"));
    }

    @Disabled
    @DisplayName("실험용 테스트...")
    @Test
    void uselessTest() {
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
