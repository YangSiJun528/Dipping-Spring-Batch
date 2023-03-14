package com.example.dipping_spring_batch.test.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestJobExecutionListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.warn("This is form beforeJob start");
        ExecutionContext jobEx = jobExecution.getExecutionContext();
        jobEx.put("1","test 1");
        jobEx.put("2","test 2");
        jobEx.put("3","test 3");
        log.warn("This is form beforeJob end");
    }

}
