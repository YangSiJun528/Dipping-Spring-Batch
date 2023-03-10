package com.example.dipping_spring_batch.test.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "`user`")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "user_name", nullable = false)
    String name;

    // scores는 연관관계의 주인(Score)의 user 필드에 해당한다.
    // cascade = ALL과 orphanRemoval = true 을 사용해 자식 엔티티의 생명주기를 관리한다.
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    List<Score> scores = new ArrayList<Score>();
}
