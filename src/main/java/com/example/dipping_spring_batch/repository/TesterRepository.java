package com.example.dipping_spring_batch.repository;

import com.example.dipping_spring_batch.domain.Tester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TesterRepository extends JpaRepository<Tester, Long> {
}
