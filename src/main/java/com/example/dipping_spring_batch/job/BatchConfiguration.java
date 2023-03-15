package com.example.dipping_spring_batch.job;

import com.example.dipping_spring_batch.TesterService;
import com.example.dipping_spring_batch.domain.Ranking;
import com.example.dipping_spring_batch.domain.Tester;
import com.example.dipping_spring_batch.repository.RankingRepository;
import com.example.dipping_spring_batch.repository.TesterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;

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
    public Job importUserJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, Step step1, Step step2, Step step3) {
        testerService.init();
        return new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .next(step2)
                .start(step3)
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
                .listener(new StepExecutionListener() { // Job ExecutionContext 나중에 더 올바른 위치로 옮겨야함
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        ExecutionContext jobContext = stepExecution.getJobExecution().getExecutionContext();
                        jobContext.put("ranks", usersRankedQueue);
                        return;
                    }
                })
                .build();
    }

    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("step2", jobRepository)
                .<Tester, Tester>chunk(chunkSize)
                .reader(testReader(testerRepository))
                .processor(new ItemProcessor<Tester, Tester>() {
                    @Override
                    public Tester process(Tester item) throws Exception {
                        return item;
                    }
                })
                .writer(testWriter(rankingRepository))
                .transactionManager(platformTransactionManager)
                .build();
    }

    @Bean
    public RepositoryItemReader<Tester> testReader(TesterRepository testerRepository) {
        log.warn(">>>>> This is testReader");
        return new RepositoryItemReaderBuilder<Tester>()
                .name("testReader")
                .repository(testerRepository)
                .methodName("findAll")
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemWriter testWriter(RankingRepository rankingRepository) {
        log.warn(">>>>> This is testWriter");
        return new RepositoryItemWriterBuilder<Ranking>()
                .repository(rankingRepository)
                .build();
    }

    @Bean
    public Step step3(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager) {
        return new StepBuilder("step3", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.warn(">>>>> This is Step3");
                    ExecutionContext jobContext = chunkContext.getStepContext().getStepExecution()
                            .getJobExecution().getExecutionContext();
                    PriorityQueue ranks = (PriorityQueue) jobContext.get("ranks");
                    ranks.forEach((rank)->{
                        log.warn(rank.toString());
                    });
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

}
