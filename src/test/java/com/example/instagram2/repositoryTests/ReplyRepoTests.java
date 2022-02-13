package com.example.instagram2.repositoryTests;


import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Reply;
import com.example.instagram2.repository.ReplyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ReplyRepoTests {

    @Autowired
    private ReplyRepository repository;

    @Test
    public void getRepliesByImageOrderByRnoTest() {
        List<Reply> result = repository.getRepliesByImageOrderByRegDate(
                Image.builder()
                        .ino(2L)
                        .build());
        for (Reply r : result){
            System.out.println(r);
        }
    }
}