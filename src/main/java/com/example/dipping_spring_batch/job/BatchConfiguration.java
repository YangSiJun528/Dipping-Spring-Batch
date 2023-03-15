package com.example.dipping_spring_batch.job;

import com.example.dipping_spring_batch.TesterService;
import com.example.dipping_spring_batch.domain.Tester;
import com.example.dipping_spring_batch.repository.TesterRepository;
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
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Slf4j
@Configuration
public class BatchConfiguration {

    private int chunkSize = 1;

    private TesterService testerService;

    private TesterRepository userRepository;

    public BatchConfiguration(TesterService testerService, TesterRepository userRepository) {
        this.testerService = testerService;
        this.userRepository = userRepository;
    }


    @Bean
    public Job importUserJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, Step step1) {
        testerService.init();
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
                      TesterRepository userRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<Tester, Tester>chunk(chunkSize)
                .reader(testReader(userRepository))
                .writer(testWriter())
                .transactionManager(platformTransactionManager)
                .build();
    }

    @Bean
    public RepositoryItemReader<Tester> testReader(TesterRepository userRepository) {
        return new RepositoryItemReaderBuilder<Tester>()
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
