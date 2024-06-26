package com.example.user_service.controller;

import com.example.user_service.dto.request.ValidateUserDTO;
import com.example.user_service.model.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_service.api_messages.APIMessages;
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

import java.util.Optional;

@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody @Valid CreateUserDTO userDTO) {

        try {
            CreatedUserDTO createdUser = userService.createUser(userDTO);

            return new SuccessResponse<>(createdUser, APIMessages.USER_CREATED, HttpStatus.CREATED)
                    .send();

        } catch (UsernameTakenException e) {
            return new ErrorResponse(e.getMessage(), HttpStatus.FORBIDDEN)
                    .send();
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateUserCredentials(@RequestBody @Valid ValidateUserDTO validateUserDTO) {

        Optional<User> user = userService.getByUsernameAndPassword(validateUserDTO.getUsername(),
                validateUserDTO.getPassword());

        if (user.isPresent()) {

            CreatedUserDTO loggedInDto = new CreatedUserDTO(user.get());

            return new SuccessResponse<>(loggedInDto, APIMessages.USER_FOUND, HttpStatus.OK).send();
        }

        return new ErrorResponse(APIMessages.USER_NOT_FOUND, HttpStatus.FORBIDDEN).send();
    }

}
