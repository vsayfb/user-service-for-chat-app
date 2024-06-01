package com.example.user_service.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.example.user_service.model.User;

@DataMongoTest
public class UserRepositoryIntTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    void findByUsername() {

        User user = new User();

        user.setUsername("foo");
        user.setPassword("bar");

        userRepository.save(user);

        Optional<User> optionalUser = userRepository.findByUsername("foo");

        assertTrue(optionalUser.isPresent());
        assertEquals(user.getUsername(), optionalUser.get().getUsername());
        assertEquals(user.getPassword(), optionalUser.get().getPassword());
        assertTrue(ObjectId.isValid(optionalUser.get().getId()));
    }
}
