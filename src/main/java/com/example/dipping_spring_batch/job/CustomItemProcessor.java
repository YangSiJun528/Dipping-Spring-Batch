//package com.example.dipping_spring_batch.job;
//
//import com.example.dipping_spring_batch.domain.Tester;
//import org.springframework.batch.core.StepExecution;
//import org.springframework.batch.item.ExecutionContext;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.stereotype.Component;
//
//import java.util.PriorityQueue;
//
//@Component
//public class CustomItemProcessor implements ItemProcessor<Tester, Tester> {
//
//    private StepExecution stepExecution;
//
//    public CustomItemProcessor(StepExecution stepExecution) {
//        this.stepExecution = stepExecution;
//    }
//
//    @Override
//    public Tester process(Tester tester) throws Exception {
//        ExecutionContext jobContext = stepExecution.getJobExecution().getExecutionContext();
//        PriorityQueue ranks = (PriorityQueue) jobContext.get("ranks");
//        ranks.add(tester);
//        return tester;
//    }
