package com.example.user_service.model;

import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Document
public class User {

    @Id
    private String id;

    @NotNull
    @Length(min = 2, max = 18)
    private String username;

    @NotNull
    private String password;

    @CreatedDate
    private Date createdAt;

}
