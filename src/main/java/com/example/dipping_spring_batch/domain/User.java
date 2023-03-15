package com.example.dipping_spring_batch.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "`user1`")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @Column(name = "user1_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "user1_name", nullable = false)
    String name;

    @Column(name = "user1_passed", nullable = false)
    Boolean passed;

    // scores는 연관관계의 주인(Score)의 user 필드에 해당한다.
    // cascade = ALL과 orphanRemoval = true 을 사용해 자식 엔티티의 생명주기를 관리한다.
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    List<Score> scores = new ArrayList<Score>();

    public BigDecimal getAverageScore() {
        BigDecimal result = getScores().stream().map(score -> score.getValue()).map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return result.divide(BigDecimal.valueOf(scores.size()), 3, RoundingMode.HALF_UP);
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", passed=" + passed +
                ", scores=" + scores +
                '}';
    }
}
