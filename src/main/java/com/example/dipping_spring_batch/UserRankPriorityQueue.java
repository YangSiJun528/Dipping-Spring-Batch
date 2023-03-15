package com.example.dipping_spring_batch;

import com.example.dipping_spring_batch.domain.Tester;

import java.util.Comparator;
import java.util.PriorityQueue;

public class UserRankPriorityQueue extends PriorityQueue {
    public UserRankPriorityQueue(Comparator comparator) {
        Comparator.comparing(Tester::getAverageScore);
    }
}
