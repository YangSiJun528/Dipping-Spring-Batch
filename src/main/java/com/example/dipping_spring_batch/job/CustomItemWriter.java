package com.example.dipping_spring_batch.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@Slf4j
public class CustomItemWriter implements ItemWriter<String> {

//    UserRepository userRepository;
//
//    public CustomItemWriter(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    @Override
    public void write(Chunk<? extends String> chunk) throws Exception {
        chunk.forEach((user) -> {
            log.warn(user.toString());
            return;
        });
    }
}