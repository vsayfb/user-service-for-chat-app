package com.example.user_service.dto.request;

import org.hibernate.validator.constraints.Length;

import com.example.user_service.validations.PasswordRules;
import com.example.user_service.validations.UsernameRules;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CreateUserDTO {

    @NotNull
    @Length(min = UsernameRules.MIN_LENGTH, max = UsernameRules.MAX_LENGTH)
    private final String username;

    @NotNull
    @Length(min = PasswordRules.MIN_LENGTH, max = PasswordRules.MAX_LENGTH)
    private final String password;
}
