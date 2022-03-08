package com.example.instagram2;

import com.example.instagram2.entity.*;
import com.example.instagram2.repository.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
@WebAppConfiguration
public class InsertDummies {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private LikesRepository likesRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private TagRepository tagRepository;

    @Disabled
    @Test
    public void insertMember() {
        IntStream.rangeClosed(1, 200).forEach(i -> {
            String gender = (i % 2 == 0) ? "남자" : "여자";
            Member member = Member.builder()
                    .email("test" + i + "@example.com")
                    .fromSocial(false)
                    .gender(gender)
                    .intro("Hello world")
                    .name(i + "번 테스트이름")
                    .password("1111")
                    .username(i + "번이름")
                    .phoneNum("010-1234-4567")
                    .profileImageUrl(UUID.randomUUID().toString())
                    .website("www.example.com")
                    .build();

            member.addMemberRole(MemberRole.USER);
            if (i > 190) {
                member.addMemberRole(MemberRole.ADMIN);
            }
            memberRepository.save(member);
        });
    }

    @Disabled
    @Test
    public void insertDummyReplies() {
        IntStream.rangeClosed(1, 200).forEach(i -> {
            Long ino = (long) (Math.random() * 90) + 1;
            Long mno = (long) (Math.random() * 200) + 1;
            Image image = Image.builder()
                    .ino(ino)
                    .build();
            Member member = Member.builder()
                    .mno(mno)
                    .build();
            Reply reply = Reply.builder()
                    .content("댓글내용 " + i)
                    .image(image)
                    .member(member)
                    .build();
            replyRepository.save(reply);
        });
    }

    @Disabled
    @Test
    public void insertFollowRelation() {
        IntStream.rangeClosed(1, 50).forEach(i -> {
            Long mno = (long) (Math.random() * 100) + 1;
            Long rnd = (long) (Math.random() * 10) + 1;
            Member fromMember = Member.builder().mno(mno).build();
            Member toMember = Member.builder().mno(mno + rnd).build();
            Follow follow = Follow.builder()
                    .fromMember(fromMember)
                    .toMember(toMember)
                    .build();
            followRepository.save(follow);
        });

    }


    @Disabled
    @Test
    void exex(){
        Optional<Member> result = memberRepository.findById(96L);
        Member member = result.get();
        member.setUsername("96username");
        memberRepository.save(member);
    }

    @Disabled
    @Test
    public void insertDummyImages() {
        IntStream.rangeClosed(1, 90).forEach(i -> {
            Long rndMno = (long) (Math.random() * 100) + 1;
            Long rndLikeCnt = (long) (Math.random() * 100) + 1;
            Image image = Image.builder()
                    .ino((long)i)
                    .ImageUrl(UUID.randomUUID().toString())
                    .caption("#성수동 #맛집")
                    .member(Member.builder()
                            .mno(rndMno)
                            .build())
                    .likeCnt(rndLikeCnt)
                    .build();

            imageRepository.save(image);
        });
    }

    @Disabled
    @Test
    public void like() {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Long tempImageId = (long) (Math.random() * 10) + 1;
            Long tempUserId = (long) (Math.random() * 100) + 1;
            likesRepository.like(tempImageId, tempUserId);
        });

    }

    @Disabled
    @Test
    public void likes() {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Long tempImageId = (long) (Math.random() * 10) + 1;
            Long tempUserId = (long) (Math.random() * 100) + 1;
            Likes like = Likes.builder()
                    .image(Image.builder().ino(tempImageId).build())
                    .member(Member.builder().mno(tempUserId).build())
                    .build();
            likesRepository.save(like);
        });
    }

    @Test
    void du(){
        Member member = memberRepository.getById(1L);
        memberRepository.delete(member);
    }

    @Disabled
    @Test
    public void InsertDummyTags(){
        IntStream.rangeClosed(1,200).forEach(i->{
            Long ino = (long)(Math.random()*150) + 1;
            Tag tag = Tag.builder()
                    .image(Image.builder()
                            .ino(ino)
                            .build())
                    .name("태그" + i)
                    .build();
            tagRepository.save(tag);
        });

    }

    @Disabled
    @Test
    public void insertDummyMember(){
        Member member = Member.builder()
                .email("aaa@naver.com")
                .fromSocial(false)
                .username("hehe")
                .profileImageUrl("imgUrlsfjkslfsdkfjl")
                .password(passwordEncoder.encode("1234"))
                .build();
        memberRepository.save(member);
    }

    @Disabled
    @Test
    public void garbage(){
        Member member = Member.builder()
                .mno(223L)
                .email("chasw@naver.com")
                .profileImageUrl("testingImgUrl")
                .phoneNum("01075319842")
                .gender("남자")
                .intro("나는 누구인가")
                .name("로니콜먼")
                .password(passwordEncoder.encode("abcABC123!@#"))
                .username("john")
                .website("www.myInfo.com")
                .build();

        memberRepository.save(member);
    }
}
