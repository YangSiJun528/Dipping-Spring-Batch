package com.example.dipping_spring_batch.job;

import com.example.dipping_spring_batch.domain.Tester;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class CustomItemReader implements ItemReader<Tester> {
    @Override
    public Tester read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return null;
    }
}
