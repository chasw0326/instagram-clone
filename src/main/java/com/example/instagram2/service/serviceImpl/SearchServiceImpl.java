package com.example.instagram2.service.serviceImpl;

import com.example.instagram2.entity.Member;
import com.example.instagram2.entity.Tag;
import com.example.instagram2.repository.MemberRepository;
import com.example.instagram2.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class SearchServiceImpl {

    private final TagRepository tagRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void search(String keyword){
        List<Member> members = memberRepository.findMembersSearch(keyword);


    }
}
