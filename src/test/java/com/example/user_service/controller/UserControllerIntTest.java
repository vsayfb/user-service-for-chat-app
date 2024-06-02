package com.example.user_service.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.user_service.api_messages.APIMessages;
import com.example.user_service.dto.request.CreateUserDTO;
import com.example.user_service.dto.request.ValidateUserDTO;
import com.example.user_service.model.User;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.hamcrest.core.Is;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
    }

    @Test
    void shouldReturnSuccess() throws Exception {

        CreateUserDTO user = new CreateUserDTO("username", "password");

        String requestBody = objectMapper.writeValueAsString(user);

        mockMvc.perform(
                post("/users")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        Is.is(APIMessages.USER_CREATED)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp",
                        Matchers.any(String.class)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username",
                        Is.is(user.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.createdAt",
                        Matchers.any(String.class)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id",
                        Matchers.any(String.class)));
    }

    @Test
    void shouldReturnErrorResponse() throws Exception {

        User existingUser = new User();

        existingUser.setUsername("username");
        existingUser.setPassword("password");

        userRepository.save(existingUser);

        CreateUserDTO user = new CreateUserDTO(existingUser.getUsername(), "password_1");

        String requestBody = objectMapper.writeValueAsString(user);

        mockMvc.perform(
                post("/users")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        Is.is(String.format("Username {%s} is already taken.", user.getUsername()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp",
                        Matchers.any(String.class)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error",
                        Is.is(HttpStatus.BAD_REQUEST.name())));
    }

    @Test
    void shouldFindUser() throws Exception {

        User user = new User();

        user.setUsername("username");
        user.setPassword("password");

        userService.createUser(new CreateUserDTO(user.getUsername(), user.getPassword()));

        ValidateUserDTO userDTO = new ValidateUserDTO(user.getUsername(), user.getPassword());

        String requestBody = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(
                post("/users/validate")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        Is.is(APIMessages.USER_FOUND)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp",
                        Matchers.any(String.class)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data",
                        Is.is(true)));
    }

    @Test
    void shouldNotFindUser() throws Exception {

        User user = new User();

        user.setUsername("username");
        user.setPassword("password");

        userService.createUser(new CreateUserDTO(user.getUsername(), user.getPassword()));

        ValidateUserDTO userDTO = new ValidateUserDTO(user.getUsername(), "diff-password");

        String requestBody = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(
                post("/users/validate")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        Is.is(APIMessages.USER_NOT_FOUND)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp",
                        Matchers.any(String.class)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error",
                        Is.is(HttpStatus.BAD_REQUEST.name())));
    }

}
