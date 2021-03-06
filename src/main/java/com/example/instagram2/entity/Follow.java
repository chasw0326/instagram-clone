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
@ToString(exclude = {"fromMember", "toMember"})
// UK 제약조건을 추가해서 한 번만 팔로우 가능하게...
@Table(uniqueConstraints = {@UniqueConstraint(
        name = "follow_uk",
        columnNames = {"fromMember", "toMember"}
)})
public class Follow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fno;

    @JsonIgnoreProperties({"images"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromMember")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member fromMember;

    @JsonIgnoreProperties({"images"})
    @ManyToOne
    @JoinColumn(name = "toMember")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member toMember;


}
