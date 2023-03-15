package com.example.dipping_spring_batch.job;

import com.example.dipping_spring_batch.domain.Tester;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.*;

import java.util.Map;
import java.util.PriorityQueue;

public class CustomItemReader implements ItemReader<Tester> {

    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.jobExecution = stepExecution.getJobExecution();
        ExecutionContext jobContext = jobExecution.getExecutionContext();
        PriorityQueue<Map<Long,Tester>> ranks = new PriorityQueue<>();
        jobContext.put("ranks", ranks);
    }

    @Override
    public Tester read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return null;
    }
}
