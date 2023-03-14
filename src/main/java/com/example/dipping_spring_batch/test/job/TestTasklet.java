package com.example.dipping_spring_batch.test.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
public class TestTasklet implements Tasklet {

    private Long limit;

    public TestTasklet (Long limit) {
        this.limit = limit;
    }


    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.warn("This is form TestTasklet, limit = { " + limit + " }");
        return RepeatStatus.FINISHED;
    }
}
