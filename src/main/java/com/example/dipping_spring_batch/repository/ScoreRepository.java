package com.example.dipping_spring_batch.repository;

import com.example.dipping_spring_batch.domain.Score;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Long> {
}
