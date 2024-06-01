package com.example.user_service.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.user_service.model.User;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username);
}
