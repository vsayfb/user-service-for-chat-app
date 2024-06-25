package com.example.user_service.model;

import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.user_service.validations.UsernameRules;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Document
public class User {

    @Id
    private String id;

    @NotNull
    @Length(min = UsernameRules.MIN_LENGTH, max = UsernameRules.MAX_LENGTH)
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String profilePicture;

    @CreatedDate
    private Date createdAt;

}
