package com.example.instagram2.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "image")
@JsonIgnoreProperties({"image"})
public class Tag extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tno;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Image image;

}
