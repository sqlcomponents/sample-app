package com.example.core.security.service;

import com.example.core.security.model.AuthenticationResponse;
import com.example.core.security.model.SignupRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
class AuthServiceTest {

    private final AuthService authService;

    /**
     * Builds Test.
     * @param authService
     */
    @Autowired
    AuthServiceTest(final AuthService authService) {
        this.authService = authService;
    }

    @Test
    void signUp() {
        AuthenticationResponse signUpResponse = getSignUpResponse();

        Assertions.assertNotNull(signUpResponse.getRegistrationToken(),"Registration Token Missing");
        Assertions.assertNull(signUpResponse.getAccessToken(),"Access Token Not Allowed for Signup");
        Assertions.assertNull(signUpResponse.getExpiresIn(),"Expires In Not Allowed for Signup");
        Assertions.assertNull(signUpResponse.getRefreshToken(),"Refresh Token Not Allowed for Signup");

    }

    private AuthenticationResponse getSignUpResponse() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUserName("Sathish");
        signupRequest.setPassword("Password");
        signupRequest.setRoles(Set.of("USER"));

        AuthenticationResponse signUpResponse = authService.signUp(signupRequest);
        return signUpResponse;
    }


}