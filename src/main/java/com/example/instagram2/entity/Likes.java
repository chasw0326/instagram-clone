package com.example.instagram2.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@ToString(exclude = {"member", "image"})
// UK를 추가해서 같은 사람이 같은 사진에 좋아요를 두 번 할수 없음
@Table(uniqueConstraints = {@UniqueConstraint(
        name = "LIKES_UK",
        columnNames = {"member", "image"}
)})
public class Likes extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lno;

    @JsonIgnoreProperties({"images"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "image")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Image image;
}
