package com.example.user_service.exception;

public class UsernameTakenException extends RuntimeException {

    public UsernameTakenException(String username) {
        super(String.format("Username {%s} is already taken.", username));
    }

}
