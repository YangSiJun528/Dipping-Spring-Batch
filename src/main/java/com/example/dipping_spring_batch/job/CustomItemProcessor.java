package com.example.dipping_spring_batch.job;

import com.example.dipping_spring_batch.domain.Tester;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.PriorityQueue;

@Component
public class CustomItemProcessor implements ItemProcessor<Tester, Tester> {

    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public Tester process(Tester tester) throws Exception {
        ExecutionContext jobContext = jobExecution.getExecutionContext();
        PriorityQueue<Tester> ranks = (PriorityQueue<Tester>) jobContext.get("ranks");
        if (ranks == null) {
            ranks = new PriorityQueue<>();
            jobContext.put("ranks", ranks);
        }
        ranks.add(tester);
        return tester;
    }
}