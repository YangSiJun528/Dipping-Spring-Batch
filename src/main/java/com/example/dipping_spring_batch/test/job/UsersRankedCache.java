package com.example.dipping_spring_batch.test.job;

import com.example.dipping_spring_batch.test.domain.User;

import java.util.*;

public class UsersRankedCache {

    private PriorityQueue<User> usersRankedCache = new PriorityQueue<>(Comparator.comparing(User::getAverageScore));

    public List<User> getUsers(Long limit) {
        return usersRankedCache.stream().limit(limit).toList();
    }

    public boolean add(User user) {
        return usersRankedCache.add(user);
    }
}
