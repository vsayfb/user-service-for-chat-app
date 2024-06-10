package com.example.user_service.contract;

import au.com.dius.pact.provider.junitsupport.State;
import com.example.user_service.controller.UserController;
import com.example.user_service.dto.request.CreateUserDTO;
import com.example.user_service.dto.response.CreatedUserDTO;
import com.example.user_service.exception.UsernameTakenException;
import com.example.user_service.model.User;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.example.user_service.service.UserService;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.spring.spring6.PactVerificationSpring6Provider;
import au.com.dius.pact.provider.spring.spring6.Spring6MockMvcTestTarget;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;


@Provider("UserService")
@PactBroker
@WebMvcTest(UserController.class)
public class AuthenticationServiceContractVerificationTest {

    private MockMvc mockMvc;

    private PactVerificationContext context;

    @MockBean
    private UserService userService;

    @TestTemplate
    @ExtendWith(PactVerificationSpring6Provider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @BeforeEach
    void before(PactVerificationContext context) {
        this.context = context;

        disableValidation();

        context.setTarget(new Spring6MockMvcTestTarget(mockMvc));
    }

    @State("invalid body")
    void invalidBody(){
        enableValidation();
    }

    @State("invalid credentials")
    void invalidCredentialsToAuthenticateUser(){
        when(userService.getByUsernameAndPassword(any(String.class), any(String.class))).thenReturn(Optional.empty());
    }

    @State("valid body with existing user")
    void validBodyWithExistingUserToCreateUser(){
        when(userService.createUser(any(CreateUserDTO.class))).thenThrow(new UsernameTakenException("username"));
    }

    @State("valid body with non-existing user")
    void validBodyWithNonExistingUserToCreateUser(){
        User user = new User();
        user.setId(ObjectId.get().toHexString());
        user.setUsername("username");

        when(userService.createUser(any(CreateUserDTO.class))).thenReturn(new CreatedUserDTO(user));
    }

    @State("valid body with valid credentials")
    void validBodyWithValidCredentialsToAuthenticateUser(){
        User user = new User();
        user.setId(ObjectId.get().toHexString());
        user.setUsername("username");

        when(userService.getByUsernameAndPassword(any(String.class), any(String.class))).thenReturn(Optional.of(user));
    }

    public void enableValidation(){
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();

        context.setTarget(new Spring6MockMvcTestTarget(mockMvc));
    }

    public void disableValidation(){
        Validator validator = Mockito.mock(LocalValidatorFactoryBean.class);

        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).setValidator(validator).build();
    }

}
