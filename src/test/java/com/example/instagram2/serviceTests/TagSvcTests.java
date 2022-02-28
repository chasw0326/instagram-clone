package com.example.instagram2.serviceTests;

import com.example.instagram2.entity.Tag;
import com.example.instagram2.service.TagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TagSvcTests {

    @Autowired
    private TagService tagService;


    @DisplayName("태그 등록순으로 정렬해서 가져오기")
    @Test
    void Should_GetTags_OrderByRegDate(){
        Long imageId = 3L;
        List<Tag> tags = tagService.getTags(imageId);
        for(Tag t : tags){
            System.out.println(t);
        }
        assertEquals(133L, tags.get(0).getTno());
        assertEquals(154L, tags.get(1).getTno());
        assertEquals(173L, tags.get(2).getTno());
    }
}
