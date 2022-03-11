package com.example.instagram2.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@ToString
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    // 중복 허용 x
    @Column(nullable = false, length = 40, unique = true)
    private String username;

    @JsonIgnore
    @Setter
    @Column(nullable = false, length = 80)
    private String password;

    @Column(length = 40)
    private String name;

    private String website;

    // 자기소개
    @Column(length = 500)
    private String intro;

    @Column(nullable = false, length = 60 ,unique = true)
    private String email;

    @Column(unique = true, length = 60)
    private String phoneNum;

    // 남성 or 여성
    private String gender;

    @Setter
    private String profileImageUrl;

    private boolean fromSocial;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    @JsonIgnore
    private Set<MemberRole> roleSet = new HashSet<>();

    /**
     * 역할을 변경하는 것이 아니라 추가하는 식으로 구현
     * @param memberRole
     */
    public void addMemberRole(MemberRole memberRole) {
        roleSet.add(memberRole);
    }

}
