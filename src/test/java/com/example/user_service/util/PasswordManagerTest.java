package com.example.user_service.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class PasswordManagerTest {

    @Autowired
    private PasswordManager passwordManager;

    @Test
    public void shouldHashPassword() {
        String password = "password";

        String encoded = passwordManager.encodePassword(password);

        assertTrue(encoded.length() == 60);
        assertFalse(password.equals(encoded));
    }

    @Test
    public void shouldCheckPassword() {
        String password = "password";

        String encoded = passwordManager.encodePassword(password);

        assertTrue(passwordManager.checkPassword(password, encoded));

        String anotherEncode = passwordManager.encodePassword("password-1");

        assertFalse(passwordManager.checkPassword(password, anotherEncode));
    }
}
