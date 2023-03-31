package com.example.core.security.service;

import com.example.core.security.model.AuthenticationResponse;
import com.example.core.security.model.LoginRequest;
import com.example.core.security.model.RefreshToken;
import com.example.core.security.model.RegistrationRequest;
import com.example.core.security.model.SignupRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {
    /**
     * authenticationManager.
     */
    private final AuthenticationManager authenticationManager;
    /**
     * encoder.
     */
    private final PasswordEncoder encoder;
    /**
     * tokenProvider.
     */
    private final TokenProvider tokenProvider;

    /**
     * UserDetailsManager.
     */
    private final UserDetailsManager userDetailsManager;

    /**
     * Keeps Registered Users.
     */
    private final Set<String> registeredUsers;

    /**
     * Builds Auth Service.
     *
     * @param theAuthenticationManager
     * @param passwordEncoder
     * @param theTokenProvider
     * @param theUserDetailsManager
     */
    public AuthService(final AuthenticationManager theAuthenticationManager,
                       final PasswordEncoder passwordEncoder,
                       final TokenProvider theTokenProvider,
                       final UserDetailsManager theUserDetailsManager) {
        this.authenticationManager = theAuthenticationManager;
        this.encoder = passwordEncoder;
        this.tokenProvider = theTokenProvider;
        this.userDetailsManager = theUserDetailsManager;


        this.registeredUsers = new HashSet<>();

    }


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

        return authenticate(signupRequest, false);
    }


    /**
     * @param loginRequest
     * @return AuthenticationResponse
     */
    public AuthenticationResponse login(
            final @Valid LoginRequest loginRequest) {

        if (!this.registeredUsers.contains(loginRequest.getUserName())) {
            throw new BadCredentialsException("User is not registeed");
        }

        return authenticate(loginRequest, true);
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
        AuthenticationResponse authenticationResponse = tokenProvider
                .register(authHeader, userName, registrationRequest);
        this.registeredUsers.add(authenticationResponse.getUserName());
        return authenticationResponse;
    }

    /**
     * refresh.
     * @param authHeader
     * @param userName
     * @param refreshToken
     * @return authenticationResponse
     */
    public AuthenticationResponse refresh(final String authHeader,
                                          final String userName,
                                          final RefreshToken refreshToken) {
        return tokenProvider.refresh(authHeader, userName, refreshToken);
    }


    /**
     * Get User Details.
     *
     * @param userName
     * @return userdetails
     */
    public UserDetails me(final String userName) {
        return userDetailsManager.loadUserByUsername(userName);
    }

    /**
     * Authendicate the request.
     * @param loginRequest
     * @param isRegistered
     * @return AuthenticationResponse
     */
    private AuthenticationResponse authenticate(
            final LoginRequest loginRequest,
            final boolean isRegistered) {
        final Authentication authResult =
                this.authenticationManager
                        .authenticate(
                                new UsernamePasswordAuthenticationToken(
                                        loginRequest.getUserName(),
                                        loginRequest.getPassword()));
        if (authResult == null) {
            throw new BadCredentialsException("Invalid Login Credentials");
        }
        return tokenProvider.getAuthenticationResponse(
                authResult.getName(),
                isRegistered);
    }
}
