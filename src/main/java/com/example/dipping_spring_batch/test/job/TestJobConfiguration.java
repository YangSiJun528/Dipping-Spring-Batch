package com.example.dipping_spring_batch.test.job;

import com.example.dipping_spring_batch.test.domain.User;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.PriorityQueue;

@Slf4j // log 사용을 위한 lombok 어노테이션
@Configuration
public class TestJobConfiguration extends DefaultBatchConfiguration {
    // DefaultBatchConfiguration 상속은 @EnableBatchProcessing과 동일한 역할을 한다.
    // @EnableBatchProcessing은 JobRepository, TransactionManager 등 여러 공통 구현 component를 불러온다.

    private int chunkSize = 1;

    @Bean
    public Job userPassDecisionJob(JobRepository jobRepository, EntityManagerFactory entityManagerFactory) {
        return new JobBuilder("userPassDecisionJob", jobRepository)
                .start(UserRankStep())
                .next(UserRankStep())
                .build();
    }

    @Bean
    @JobScope
    public Step userRankstep(JobRepository jobRepository, EntityManagerFactory entityManagerFactory, @Value("#{jobParameters['count']}") Long limit) {
        return new StepBuilder("userRankingStep", jobRepository)
                .<User, User>chunk(chunkSize)
                .reader(UserRankReader(entityManagerFactory))
                .writer(UserRankWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<User> userRankReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<User>()
                .name("jpaPagingItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT p FROM User ORDER BY id")
                .build();
    }

    @Bean
    public ItemWriter<User> userRankWriter() {
        return null;
    }

    @Bean
    @JobScope
    public Step userPassByRankStep(JobRepository jobRepository, EntityManagerFactory entityManagerFactory, @Value("#{jobParameters['count']}") Long limit) {
        return new StepBuilder("userRankingStep", jobRepository)
                .<User, User>chunk(chunkSize)
                .reader(UserPassByRankReader(entityManagerFactory))
                .writer(UserPassByRankWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<User> userPassByRankReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<User>()
                .name("jpaPagingItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT p FROM User ORDER BY id")
                .build();
    }

    @Bean
    public ItemWriter<User> userPassByRankWriter() {
        return null;
    }
}
