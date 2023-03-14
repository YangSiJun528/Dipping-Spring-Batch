package com.example.dipping_spring_batch.test.job;

import com.example.dipping_spring_batch.test.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Comparator;
import java.util.PriorityQueue;

@Slf4j // log 사용을 위한 lombok 어노테이션
@Configuration
public class TestJobConfiguration extends DefaultBatchConfiguration {
    // DefaultBatchConfiguration 상속은 @EnableBatchProcessing과 동일한 역할을 한다.
    // @EnableBatchProcessing은 JobRepository, TransactionManager 등 여러 공통 구현 component를 불러온다.

    private int chunkSize = 1;

    private PriorityQueue<User> sortedUserRank = new PriorityQueue<>(Comparator.comparing(User::getAverageScore));

    @Bean
    public Job testJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, JobExecutionListener listener) {
        log.warn("This is form testJob");
        return new JobBuilder("testJob", jobRepository)
                .listener(listener)
                .flow(step1(jobRepository, platformTransactionManager))
                .end()
                .build();
    }

    // Tasklet나 Writer 같이 Step의 구성요소가 Config 안에 선언되지 않은 경우 인자로 들어간다.
    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        log.warn("This is form step1");
        return new StepBuilder("step1", jobRepository)
                .tasklet(testTasklet(), platformTransactionManager)
                .build();
    }

    @Bean
    public Tasklet testTasklet() {
        Tasklet tasklet = new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                log.warn("This is form TestTasklet");
                return RepeatStatus.FINISHED;
            }
        };
        return tasklet;
    }

}
