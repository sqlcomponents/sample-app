package com.example.app.service;

import com.example.core.payload.LoginRequest;
import com.example.core.payload.SignupRequest;
import com.example.core.payload.JwtResponse;
import com.example.core.security.UserDetailsService;
import com.example.core.service.AuthService;
import org.example.MovieManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.SQLException;
import java.util.Set;

@SpringBootTest
class AuthServiceTest {

    public static final String USER_NAME = "UserName";
    @Autowired
    private AuthService authService;

    @Autowired
    private MovieManager movieManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @BeforeEach
    void before() throws SQLException {
        this.cleanUp();
    }

    @AfterEach
    void after() throws SQLException {
        this.cleanUp();
    }

    void cleanUp() throws SQLException {
        movieManager.getUserRolesStore().delete().execute();
        movieManager.getUsersStore().delete().execute();
    }


    @Test
    public void testAuthWithRole() throws SQLException {

        SignupRequest signupRequest = aSignupRequesr();
        signupRequest.setRole(Set.of("ROLE_ADMIN"));

        JwtResponse jwtResponse = registerUser(signupRequest);

        Assertions.assertTrue(jwtResponse.getRoles().containsAll(signupRequest.getRole()), "JWT Roles are available");

        UserDetails userDetails = authService.me(jwtResponse.getAccessToken());

        Assertions.assertTrue(userDetails.getUsername().equals(USER_NAME), "User Profile is available");

    }

    @Test
    public void testAuthWithInvalidole()  {

        SignupRequest signupRequest = aSignupRequesr();
        signupRequest.setRole(Set.of("ROLE_NOT_AVAILABLE"));

        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            JwtResponse jwtResponse = registerUser(signupRequest);
        });

    }


    @Test
    void testLogout() throws SQLException {
        SignupRequest signupRequest = aSignupRequesr();

        JwtResponse jwtResponse = registerUser(signupRequest);

        authService.logout(jwtResponse.getAccessToken());

        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            authService.me(jwtResponse.getAccessToken());
        });

    }

    @Test
    public void testAuthWithoutRole() throws SQLException {

        SignupRequest signupRequest = aSignupRequesr();

        JwtResponse jwtResponse = registerUser(signupRequest);

        Assertions.assertTrue(jwtResponse.getRoles().contains("ROLE_USER"), "JWT Default Roles are available");

    }

    private JwtResponse registerUser(final SignupRequest signupRequest) throws SQLException {
        authService.register(signupRequest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(signupRequest.getUsername());
        loginRequest.setPassword(signupRequest.getPassword());
        JwtResponse jwtResponse = authService.authenticate(loginRequest);
        return jwtResponse;
    }

    private SignupRequest aSignupRequesr() {
        final String userName = USER_NAME;
        final String password = System.currentTimeMillis() + "";

        SignupRequest signupRequest = new SignupRequest();

        signupRequest.setUsername(userName);
        signupRequest.setEmail("user@email.com");
        signupRequest.setPassword(password);
        return signupRequest;
    }
}