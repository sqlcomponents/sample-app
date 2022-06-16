package com.example.controllers;

import com.example.payload.LoginRequest;
import com.example.payload.SignupRequest;
import com.example.payload.JwtResponse;
import com.example.payload.MessageResponse;
import com.example.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.sql.SQLException;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * Auth Service.
     */
    private final AuthService authService;

    /**
     * Builds Auth Controller.
     * @param anAuthService
     */
    public AuthController(final AuthService anAuthService) {
        this.authService = anAuthService;
    }
    /**
     * signin an signin.
     * @param loginRequest
     * @return  loginRequest
     */
    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(final @Valid
                               @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticate(loginRequest));
    }

    /**
     * @param signUpRequest
     * @return loginRequest
     */
    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(final @Valid
                                     @RequestBody SignupRequest signUpRequest)
                                      throws SQLException {
        return ResponseEntity.ok(authService.register(signUpRequest));
    }
}
