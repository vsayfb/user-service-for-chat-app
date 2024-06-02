package com.example.user_service.dto.response;

import java.util.Date;

import com.example.user_service.model.User;

import lombok.Getter;

@Getter
public class CreatedUserDTO {

    private final String id;

    private final String username;

    private final Date createdAt;

    public CreatedUserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.createdAt = user.getCreatedAt();
    }
}
