package com.example.dipping_spring_batch.job;

import com.example.dipping_spring_batch.domain.Tester;
import com.example.dipping_spring_batch.util.BoundedArray;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class CustomJobExecutionListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        BoundedArray<Tester> boundedArray = new BoundedArray<>(100, Comparator.comparing(Tester::getAverageScore));  // 실제로는 Job Paramater나 인수로 받아서 해야함
        executionContext.put("ranks", boundedArray);
    }

}
