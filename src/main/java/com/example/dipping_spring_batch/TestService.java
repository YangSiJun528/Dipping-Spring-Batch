package com.example.dipping_spring_batch;

import com.example.dipping_spring_batch.repository.UserRepository;

public class TestService {
    private UserRepository userRepository;

    public TestService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


}
