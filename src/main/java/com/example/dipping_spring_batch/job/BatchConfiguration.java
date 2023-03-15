package com.example.dipping_spring_batch.job;

import com.example.dipping_spring_batch.domain.User;
import com.example.dipping_spring_batch.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class BatchConfiguration {

    private int chunkSize = 1;

    private UserRepository userRepository;

    private CustomItemWriter customItemWriter;

    public BatchConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, Step step1) {
        return new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .next(step2(jobRepository, userRepository, platformTransactionManager))
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.warn(">>>>> This is Step1");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step step2(JobRepository jobRepository,
                      UserRepository userRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<User, User>chunk(chunkSize)
                .reader(testReader(userRepository))
                .writer(testWriter())
                .transactionManager(platformTransactionManager)
                .build();
    }

    @Bean
    public RepositoryItemReader<User> testReader(UserRepository userRepository) {
        return new RepositoryItemReaderBuilder<User>()
                .name("testReader")
                .repository(userRepository)
                .methodName("findAll")
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemWriter testWriter() {
        log.warn(">>>>> This is testWriter");
        return new CustomItemWriter();
    }
}
