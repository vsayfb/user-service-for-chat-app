package com.example.user_service.service;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.user_service.dto.request.CreateUserDTO;
import com.example.user_service.dto.response.CreatedUserDTO;
import com.example.user_service.exception.UsernameTakenException;
import com.example.user_service.model.User;
import com.example.user_service.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUserIfNotExist() {

        CreateUserDTO userDTO = new CreateUserDTO("username", "password");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(null));

        CreatedUserDTO createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        assertEquals(createdUser.getUsername(), userDTO.getUsername());
    }

    @Test
    void shouldThrowErrorIfUserExists() {

        CreateUserDTO userDTO = new CreateUserDTO("username", "password");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(new User()));

        assertThatExceptionOfType(UsernameTakenException.class)
                .isThrownBy(() -> userService.createUser(userDTO))
                .withMessageContaining("already taken.");
    }
}
