package com.example.dipping_spring_batch;

import com.example.dipping_spring_batch.domain.Score;
import com.example.dipping_spring_batch.domain.Tester;
import com.example.dipping_spring_batch.repository.ScoreRepository;
import com.example.dipping_spring_batch.repository.TesterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TesterService {

    private TesterRepository testerRepository;

    private ScoreRepository scoreRepository;

    public TesterService(TesterRepository testerRepository, ScoreRepository scoreRepository) {
        this.testerRepository = testerRepository;
        this.scoreRepository = scoreRepository;
    }

    public void init() {
        Score score11 = Score.builder()
                .id(1L)
                .value(100L)
                .build();
        Score score12 = Score.builder()
                .id(2L)
                .value(11L)
                .build();
        Score score13 = Score.builder()
                .id(3L)
                .value(70L)
                .build();

        scoreRepository.saveAll(List.of(score11, score12, score13));

        Tester tester1 = Tester.builder()
                .id(1L)
                .passed(false)
                .name("first")
                .scores(List.of(score11, score12, score13))
                .build();

        testerRepository.save(tester1);

        Score score21 = Score.builder()
                .id(4L)
                .value(8L)
                .build();
        Score score22 = Score.builder()
                .id(5L)
                .value(77L)
                .build();
        Score score23 = Score.builder()
                .id(6L)
                .value(15L)
                .build();

        scoreRepository.saveAll(List.of(score21, score22, score23));

        Tester tester2 = Tester.builder()
                .id(2L)
                .passed(false)
                .name("second")
                .scores(List.of(score21, score22, score23))
                .build();

        testerRepository.save(tester2);

        Score score31 = Score.builder()
                .id(7L)
                .value(10L)
                .build();
        Score score32 = Score.builder()
                .id(8L)
                .value(200L)
                .build();
        Score score33 = Score.builder()
                .id(9L)
                .value(40L)
                .build();

        Tester tester3 = Tester.builder()
                .id(3L)
                .passed(false)
                .name("third")
                .scores(List.of(score31, score32, score33))
                .build();

        testerRepository.save(tester3);
    }
}
