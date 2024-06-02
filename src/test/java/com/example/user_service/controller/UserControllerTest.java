package com.example.user_service.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.user_service.dto.request.CreateUserDTO;
import com.example.user_service.dto.response.CreatedUserDTO;
import com.example.user_service.exception.UsernameTakenException;
import com.example.user_service.model.User;
import com.example.user_service.response_entity.ErrorResponse;
import com.example.user_service.response_entity.SuccessResponse;
import com.example.user_service.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void shouldReturnSuccessResponse() {

        CreateUserDTO userDTO = new CreateUserDTO("username", "password");

        when(userService.createUser(any(CreateUserDTO.class))).thenReturn(new CreatedUserDTO(new User()));

        var response = (ResponseEntity<SuccessResponse<CreatedUserDTO>>) userController.create(userDTO);

        assertNotNull(response.getBody());
        assertEquals(response.getBody().getHttpStatus(), HttpStatus.CREATED);
    }

    @Test
    void sholdReturnErrorResponse() {

        CreateUserDTO userDTO = new CreateUserDTO("username", "password");

        when(userService.createUser(any(CreateUserDTO.class))).thenThrow(new UsernameTakenException("username"));

        var response = (ResponseEntity<ErrorResponse>) userController.create(userDTO);

        assertNotNull(response.getBody());
        assertEquals(response.getBody().getError(), HttpStatus.BAD_REQUEST);
    }
}