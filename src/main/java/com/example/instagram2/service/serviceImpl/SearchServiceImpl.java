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

/**
 * <code>ReplyService</code><br>
 * 멤버에 관련된 서비스
 *
 * @author chasw326
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class SearchServiceImpl {

    private final MemberRepository memberRepository;

    /**
     * 아직 미구현<br>
     * 사용자이름이나 자기소개에서 검색합니다.
     * @param keyword (검색키워드)
     */
    @Transactional
    public void search(String keyword){
        List<Member> members = memberRepository.findMembersSearch(keyword);

    }
}
