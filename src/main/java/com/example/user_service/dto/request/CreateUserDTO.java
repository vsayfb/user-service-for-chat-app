package com.example.user_service.dto.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CreateUserDTO {

    @NotNull
    @Length(min = 2, max = 18)
    private final String username;

    @NotNull
    @Length(min = 8, max = 40)
    private final String password;
}
