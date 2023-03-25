package com.example.core.security.service;

import com.example.core.payload.AuthenticationResponse;
import com.example.core.payload.LoginRequest;
import com.example.core.payload.RegistrationRequest;
import com.example.core.payload.SignupRequest;
import com.example.core.security.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;

@Service
public class AuthService {

    /**
     * encoder.
     */
    @Autowired
    private PasswordEncoder encoder;
    /**
     * jwtUtils.
     */
    @Autowired
    private TokenProvider tokenProvider;

    /**
     * UserDetailsManager.
     */
    @Autowired
    private UserDetailsManager userDetailsManager;


    /**
     * @param signupRequest
     * @return jwt
     */
    public AuthenticationResponse signUp(
            final @Valid SignupRequest signupRequest) {

        UserDetails user = User.builder()
                .username(signupRequest.getUserName())
                .password(encoder.encode(signupRequest.getPassword()))
                .roles(signupRequest.getRoles()
                        .toArray(new String[signupRequest.getRoles().size()]))
                .build();

        userDetailsManager.createUser(user);

        return tokenProvider.authenticate(signupRequest);
    }


    /**
     * @param loginRequest
     * @return jwt
     */
    public AuthenticationResponse login(
            final @Valid LoginRequest loginRequest) {
        return tokenProvider.authenticate(loginRequest);
    }

    /**
     * @param token
     */
    public void logout(final String token) {
        tokenProvider.logout(token);
    }

    /**
     * refresh.
     * @param authHeader
     * @param userName
     * @param registrationRequest
     * @return authenticationResponse
     */
    public AuthenticationResponse register(final String authHeader,
                                           final String userName,
                                           final RegistrationRequest registrationRequest) {
        return tokenProvider.register(authHeader, userName, registrationRequest);
    }

    //    /**
//     * Get User Details.
//     *
//     * @param token
//     * @return userdetails
//     */
//    public UserDetails me(final String token) {
//        return tokenProvider.me(token);
//    }

}
