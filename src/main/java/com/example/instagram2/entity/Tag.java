package com.example.instagram2.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@ToString(exclude = "image")
public class Tag extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tno;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image")
    private Image image;

}
