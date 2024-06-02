package com.example.user_service.model;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class User {

    @Id
    private String id;

    private String username;

    private String password;

    @CreatedDate
    private Date createdAt;

}
