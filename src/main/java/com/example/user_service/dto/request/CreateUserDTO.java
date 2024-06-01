package com.example.user_service.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CreateUserDTO {

    private final String username;

    private final String password;
}
