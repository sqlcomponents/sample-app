package com.example.core.service;

import com.example.core.payload.JwtResponse;
import com.example.core.payload.LoginRequest;
import com.example.core.security.jwt.JwtUtils;
import org.example.MovieManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;

@Service
public class AuthService {

    /**
     * MovieManager.
     */
    @Autowired
    private MovieManager movieManager;

    /**
     * encoder.
     */
    @Autowired
    private PasswordEncoder encoder;
    /**
     * jwtUtils.
     */
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * @param loginRequest
     * @return jwt
     */
    public JwtResponse authenticate(final @Valid LoginRequest loginRequest) {
        return jwtUtils.authenticate(loginRequest);
    }

    /**
     * @param token
     */
    public void logout(final String token) {
        jwtUtils.logout(token);
    }

    /**
     * Get User Details.
     *
     * @param token
     * @return userdetails
     */
    public UserDetails me(final String token) {
        return jwtUtils.me(token);
    }

}
