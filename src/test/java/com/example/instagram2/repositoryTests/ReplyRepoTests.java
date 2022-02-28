package com.example.instagram2.repositoryTests;


import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Reply;
import com.example.instagram2.repository.ReplyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@SpringBootTest
public class ReplyRepoTests {

    @Autowired
    private ReplyRepository repository;

    @Test
    public void getRepliesByImageOrderByRnoTest() {
        List<Reply> result = repository.getRepliesByImageOrderByRegDateAsc(
                Image.builder()
                        .ino(2L)
                        .build(), Pageable.unpaged());
        for (Reply r : result){
            System.out.println(r);
        }
    }

    @Test
    void ex(){
        List<Reply> result = repository.getTop3ByImage_InoOrderByRegDate(4L);
        for (Reply r : result){
            System.out.println("----------------");
            System.out.println(r.getMember().getUsername());
            System.out.println(r);
        }
    }
}