package com.example.user_service.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.user_service.dto.request.CreateUserDTO;
import com.example.user_service.dto.request.ValidateUserDTO;
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

        assertEquals(response.getBody().getHttpStatus(), HttpStatus.CREATED);
    }

    @Test
    void sholdReturnErrorResponse() {

        CreateUserDTO userDTO = new CreateUserDTO("username", "password");

        when(userService.createUser(any(CreateUserDTO.class))).thenThrow(new UsernameTakenException("username"));

        var response = (ResponseEntity<ErrorResponse>) userController.create(userDTO);

        assertNotNull(response.getBody());
        assertEquals(response.getBody().getError(), HttpStatus.FORBIDDEN);
    }

    @Test
    void shouldFindUser() {

        ValidateUserDTO userDTO = new ValidateUserDTO("username", "password");

        User dummyUser = new User();

        dummyUser.setUsername("username");
        dummyUser.setId("21421849");

        when(userService.getByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword()))
                .thenReturn(Optional.of(dummyUser));

        var response = (ResponseEntity<SuccessResponse<HashMap<String, String>>>) userController.validateUserCredentials(userDTO);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getData().get("username"));
        assertNotNull(response.getBody().getData().get("id"));
    }

    @Test
    void shouldNotFindUser() {

        ValidateUserDTO userDTO = new ValidateUserDTO("username", "password");

        when(userService.getByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword()))
                .thenReturn(Optional.empty());

        var response = (ResponseEntity<ErrorResponse>) userController.validateUserCredentials(userDTO);

        assertEquals(response.getBody().getError(), HttpStatus.FORBIDDEN);
    }

}
