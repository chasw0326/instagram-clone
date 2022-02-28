package com.example.instagram2.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@ToString
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
    private Member fromMember;

    @JsonIgnoreProperties({"images"})
    @ManyToOne
    @JoinColumn(name = "toMember")
    private Member toMember;


}
