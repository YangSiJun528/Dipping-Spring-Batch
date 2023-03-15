package com.example.dipping_spring_batch.job;

import com.example.dipping_spring_batch.domain.Tester;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class CustomItemWriter implements ItemWriter<Tester> {
    @Override
    public void write(Chunk<? extends Tester> chunk) throws Exception {

    }
}
