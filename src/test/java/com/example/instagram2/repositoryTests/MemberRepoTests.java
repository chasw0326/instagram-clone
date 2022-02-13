package com.example.instagram2.repositoryTests;


import com.example.instagram2.entity.Member;
import com.example.instagram2.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MemberRepoTests {

    @Autowired
    private MemberRepository repository;

    @Test
    public void findByUserNameTest(){
        String username= "1번이름";
        Optional<Member> result = repository.findByEmailAndSocial(username, false);
        if(result.isPresent()){
            Member member = result.get();
            System.out.println("member = " + member.toString());
        }
    }

    @Test
    public void getProfileImgAndUsername(){
        Object result = repository.getUsernameById(1L);

        Object[] arr = (Object[]) result;
        System.out.println((String)arr[0]);
        System.out.println((String)arr[1]);
        assertEquals("aa3670f2-7c01-485d-a19a-5e65bfdcfc39", (String)arr[0]);
        assertEquals("1번이름", (String)arr[1]);
    }

    @Test
    public void findByEmail(){
        Optional<Member> result = repository.findByEmail("bcaaeaa@naver.com");
        if(result.isPresent()){
            Member member = result.get();
            System.out.println("member= " + member);
            assertEquals(215, member.getMno());
        }
    }

    @Test
    public void findByEmailAndSocial(){
        Optional<Member> result = repository.findByEmailAndSocial("bcaa@naver.com", false);
        if(result.isPresent()){
            Member member = result.get();
            System.out.println("member= " + member);
        }
    }

}
