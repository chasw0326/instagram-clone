package com.example.instagram2.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"member"})
@Entity
@JsonIgnoreProperties({"member"})
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ino;

    @Column(length = 500)
    private String caption;

    @Column(nullable = false)
    private String ImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    // 필요할때 likes 에서 값을 가져와서 넣어줌
    @Transient
    @Setter
    private Long likeCnt;

    // 내가 좋아요를 했는지 안 했는지 알려줌
    @Transient
    @Setter
    private boolean likeState;

}
