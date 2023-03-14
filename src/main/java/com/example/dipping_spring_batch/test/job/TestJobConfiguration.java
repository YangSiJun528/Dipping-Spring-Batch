package com.example.dipping_spring_batch.test.job;

import com.example.dipping_spring_batch.test.domain.User;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.builder.TaskletStepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
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
    public Step testStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, @Value("#{jobParameters[limit]}")Long limit) {
        log.warn("This is form testStep");
        return new StepBuilder("testJob", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.warn(">>>>> This is Step1");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Job testJob(JobRepository jobRepository, JobExecutionListener listener, Step testStep) {
        log.warn("This is form testJob");
        return new JobBuilder("testJob", jobRepository)
                .listener(listener)
                .flow(testStep)
                .end()
                .build();
    }

}
