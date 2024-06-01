package com.example.user_service.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.user_service.dto.request.CreateUserDTO;
import com.example.user_service.dto.response.CreatedUserDTO;
import com.example.user_service.exception.UsernameTakenException;
import com.example.user_service.model.User;
import com.example.user_service.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CreatedUserDTO createUser(CreateUserDTO userDTO) throws UsernameTakenException {

        Optional<User> user = userRepository.findByUsername(userDTO.getUsername());

        if (user.isPresent()) {
            throw new UsernameTakenException(userDTO.getUsername());
        }

        User newUser = new User();

        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(userDTO.getPassword());

        userRepository.save(newUser);

        return new CreatedUserDTO(newUser.getUsername());
    }
}
