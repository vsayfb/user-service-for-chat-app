package com.example.user_service.exception;

import org.springframework.http.HttpStatus;

public class UsernameTakenException extends RuntimeException {

    private final HttpStatus status = HttpStatus.NOT_FOUND;

    public UsernameTakenException(String username) {
        super(String.format("Username {%s} is already taken.", username));
    }

    public HttpStatus getStatus() {
        return status;
    }
}
