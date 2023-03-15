package com.example.dipping_spring_batch.repository;

import com.example.dipping_spring_batch.domain.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {
}
