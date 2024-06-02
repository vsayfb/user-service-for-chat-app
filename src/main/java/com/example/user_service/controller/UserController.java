package com.example.user_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_service.dto.request.CreateUserDTO;
import com.example.user_service.dto.response.CreatedUserDTO;
import com.example.user_service.exception.UsernameTakenException;
import com.example.user_service.response_entity.ErrorResponse;
import com.example.user_service.response_entity.SuccessResponse;
import com.example.user_service.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequestMapping("users")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid CreateUserDTO userDTO) {

        try {
            CreatedUserDTO createdUser = userService.createUser(userDTO);

            return new SuccessResponse<>(createdUser, "User is created successfully.", HttpStatus.CREATED)
                    .send();

        } catch (UsernameTakenException e) {
            return new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST)
                    .send();
        }
    }

}
