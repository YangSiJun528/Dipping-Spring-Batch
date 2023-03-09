package com.example.dipping_spring_batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class DippingSpringBatchApplication {

    public static void main(String[] args) {

        //SpringApplication.run(DippingSpringBatchApplication.class, args);
        System.exit(SpringApplication.exit(SpringApplication.run(DippingSpringBatchApplication.class, args)));
    }

}
