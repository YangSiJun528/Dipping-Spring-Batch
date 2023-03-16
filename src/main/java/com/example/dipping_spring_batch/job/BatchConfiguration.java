package com.example.dipping_spring_batch.job;

import com.example.dipping_spring_batch.TesterService;
import com.example.dipping_spring_batch.domain.Ranking;
import com.example.dipping_spring_batch.domain.Tester;
import com.example.dipping_spring_batch.repository.RankingRepository;
import com.example.dipping_spring_batch.repository.TesterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;

import static org.springframework.boot.context.properties.bind.Bindable.mapOf;

@Slf4j
@Configuration
public class BatchConfiguration {

    private int chunkSize = 1;

    private PriorityQueue<Tester> usersRankedQueue = new PriorityQueue<>(Comparator.comparing(Tester::getAverageScore));

    private TesterService testerService;

    private TesterRepository testerRepository;

    private RankingRepository rankingRepository;

    public BatchConfiguration(TesterService testerService, TesterRepository testerRepository, RankingRepository rankingRepository) {
        this.testerService = testerService;
        this.testerRepository = testerRepository;
        this.rankingRepository = rankingRepository;
    }

    @Bean
    public Job jobParameterJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, Step step1) {
        return new JobBuilder("jobParameterJob", jobRepository)
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, Tasklet testTasklet) {
        return new StepBuilder("step1", jobRepository)
                .tasklet(testTasklet)
                .transactionManager(platformTransactionManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet testTasklet(PlatformTransactionManager platformTransactionManager, @Value("#{jobParameters[param1]}") String param) {
        return (stepContribution, chunkContext) -> {
            log.warn(">>>>>>tasklet");
            log.warn(param);
            return RepeatStatus.FINISHED;
        };
    }
}
