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
import com.example.user_service.util.PasswordManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordManager passwordManager;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUserIfNotExist() {

        CreateUserDTO userDTO = new CreateUserDTO("username", "password");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        User result = new User();

        result.setUsername(userDTO.getUsername());

        when(userRepository.save(any(User.class))).thenReturn(result);

        CreatedUserDTO createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        assertEquals(result.getUsername(), userDTO.getUsername());
    }

    @Test
    void shouldThrowErrorIfUserExists() {

        CreateUserDTO userDTO = new CreateUserDTO("username", "password");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(new User()));

        assertThatExceptionOfType(UsernameTakenException.class)
                .isThrownBy(() -> userService.createUser(userDTO))
                .withMessageContaining("already taken.");
    }

    @Test
    void shouldFindUser() {

        User user = new User();

        user.setUsername("username");
        user.setPassword("password");
        user.setProfilePicture("http://picture");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordManager.checkPassword(anyString(), anyString())).thenReturn(true);

        Optional<User> optional = userService.getByUsernameAndPassword(user.getUsername(), user.getPassword());

        assertTrue(optional.isPresent());

        assertEquals(optional.get().getUsername(), user.getUsername());
        assertEquals(optional.get().getPassword(), user.getPassword());
    }

    @Test
    void shouldNotFindUser() {

        User user = new User();

        user.setUsername("username");
        user.setPassword("password");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordManager.checkPassword(anyString(), anyString())).thenReturn(false);

        Optional<User> optional = userService.getByUsernameAndPassword(user.getUsername(), user.getPassword());

        assertTrue(optional.isEmpty());

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordManager.checkPassword(anyString(), anyString())).thenReturn(false);

        Optional<User> optionalUser = userService.getByUsernameAndPassword(user.getUsername(), user.getPassword());

        assertTrue(optionalUser.isEmpty());
    }
}
