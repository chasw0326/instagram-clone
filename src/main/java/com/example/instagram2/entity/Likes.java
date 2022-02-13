package com.example.instagram2.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@ToString(exclude = {"member", "image"})
@Table(uniqueConstraints = @UniqueConstraint(
        name = "LIKES_UK",
        columnNames = {"member", "image"}))
public class Likes extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lno;

    @JsonIgnoreProperties({"images"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "image")
    private Image image;
}
