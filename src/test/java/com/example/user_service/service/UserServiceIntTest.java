package com.example.user_service.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.user_service.dto.request.CreateUserDTO;
import com.example.user_service.dto.response.CreatedUserDTO;
import com.example.user_service.exception.UsernameTakenException;
import com.example.user_service.model.User;
import com.example.user_service.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
public class UserServiceIntTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUserIfNotExist() {

        CreateUserDTO userDTO = new CreateUserDTO("username", "password");

        CreatedUserDTO createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);

        assertEquals(createdUser.getUsername(), userDTO.getUsername());
    }

    @Test
    void shouldThrowErrorIfUserExists() {

        User existingUser = new User();

        existingUser.setUsername("username");
        existingUser.setPassword("password");

        userRepository.save(existingUser);

        CreateUserDTO userDTO = new CreateUserDTO(existingUser.getUsername(), "pass");

        assertThatExceptionOfType(UsernameTakenException.class)
                .isThrownBy(() -> userService.createUser(userDTO))
                .withMessageContaining("already taken.");
    }

}
