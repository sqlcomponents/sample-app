package com.example.security.service;

import com.example.payload.request.LoginRequest;
import com.example.payload.request.SignupRequest;
import com.example.payload.response.JwtResponse;
import org.example.MovieManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MovieManager movieManager;


    @Test
    public void testAuth() throws SQLException {

        movieManager.getUserRolesStore().deleteAll();
        movieManager.getUsersStore().deleteAll();

        final String userName = "UserName";
        final String password = System.currentTimeMillis() + "";

        SignupRequest signupRequest = new SignupRequest();

        signupRequest.setUsername(userName);
        signupRequest.setEmail("user@email.com");
        signupRequest.setPassword(password);

        authService.register(signupRequest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(userName);
        loginRequest.setPassword(password);
        JwtResponse jwtResponse = authService.authendicate(loginRequest);

        Assertions.assertEquals(jwtResponse.getUsername(),userName,"JWT is not working");

    }
}