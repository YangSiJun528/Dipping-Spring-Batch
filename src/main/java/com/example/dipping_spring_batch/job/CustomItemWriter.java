package com.example.dipping_spring_batch.job;

import com.example.dipping_spring_batch.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class CustomItemWriter implements ItemWriter<String> {

//    @Autowired
//    UserRepository userRepository;    @Autowired
//    UserRepository userRepository;

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