package com.example.user_service.dto.request;

public class ValidateUserDTO extends CreateUserDTO {

    public ValidateUserDTO(String username, String password) {
        super(username, password);
    }

}
