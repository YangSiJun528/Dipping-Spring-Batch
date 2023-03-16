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
import org.springframework.batch.core.scope.context.StepContext;
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

    //PriorityQueue 이거 사용하는게 맞나? 그냥 ArrayList 사용해서 하는게 더 나을듯?

    @Bean
    public Job importUserJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, Step step1, Step step2, Step step3) {
        testerService.init();
        return new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .next(step2)
                .next(step3)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("step1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    // Step ExecutionContext에 데이터 저장
                    ExecutionContext jobExecution = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
                    jobExecution.put("number", 10);
                    return RepeatStatus.FINISHED;
                }, platformTransactionManager)
                .build();
    }
        @Bean
        public Step step2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
            return new StepBuilder("step2", jobRepository)
                    .<Tester, Tester>chunk(chunkSize)
                    .listener(new CustomItemProcessor())
                    .reader(new RepositoryItemReaderBuilder<Tester>()
                            .repository(testerRepository)
                            .methodName("findAll")
                            .sorts(Map.of("id", Sort.Direction.ASC))
                            .pageSize(chunkSize)
                            .build())
                    .processor(new CustomItemProcessor())
                    .writer(new RepositoryItemWriterBuilder<Tester>()
                            .repository(testerRepository)
                            .methodName("save")
                            .build())
                    .transactionManager(platformTransactionManager)
                    .build();
    }
        @Bean
        public Step step3(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
            return new StepBuilder("step3", jobRepository)
                    .tasklet((contribution, chunkContext) -> {
                        // Step ExecutionContext에서 데이터 가져오기
                        ExecutionContext executionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
                        int number = (int) executionContext.get("number");
                        log.warn("Number from Step ExecutionContext: " + number);
                        PriorityQueue<Map<Long,Tester>> ranks = (PriorityQueue<Map<Long, Tester>>) executionContext.get("ranks");
                        ranks.forEach((rank)->{
                            ranks.poll();
                        });
                        return RepeatStatus.FINISHED;
                    }, platformTransactionManager)
                    .build();
    }
}
