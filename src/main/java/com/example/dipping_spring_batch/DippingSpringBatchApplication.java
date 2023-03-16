package com.example.dipping_spring_batch;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class DippingSpringBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(DippingSpringBatchApplication.class, args);

    }

}
