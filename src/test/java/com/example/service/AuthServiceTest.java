package com.example.service;

import com.example.payload.LoginRequest;
import com.example.payload.SignupRequest;
import com.example.payload.JwtResponse;
import org.example.MovieManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.Set;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MovieManager movieManager;


    @Test
    public void testAuth() throws SQLException {

        movieManager.getUserRolesStore().delete().execute();
        movieManager.getUsersStore().delete().execute();

        final String userName = "UserName";
        final String password = System.currentTimeMillis() + "";

        SignupRequest signupRequest = new SignupRequest();

        signupRequest.setUsername(userName);
        signupRequest.setEmail("user@email.com");
        signupRequest.setPassword(password);
        signupRequest.setRole(Set.of("ROLE_ADMIN"));

        authService.register(signupRequest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(userName);
        loginRequest.setPassword(password);
        JwtResponse jwtResponse = authService.authenticate(loginRequest);

        Assertions.assertTrue(jwtResponse.getRoles().contains("ROLE_ADMIN"), "JWT is not working");

    }
}