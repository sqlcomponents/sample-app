package com.example.core.security.controllers;

import com.example.core.security.model.RefreshToken;
import com.example.core.security.model.RegistrationRequest;
import com.example.core.security.model.SignupRequest;
import com.example.core.security.model.AuthenticationResponse;
import com.example.core.security.model.LoginRequest;
import com.example.core.security.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication",
        description = "Resource to manage authentication")
class AuthAPIController {

    /**
     * Auth Service.
     */
    private final AuthService authService;

    /**
     * Builds Auth Controller.
     * @param anAuthService
     */
    AuthAPIController(final AuthService anAuthService) {
        this.authService = anAuthService;
    }

    /**
     * signup an signup.
     * @param signupRequest
     * @return  loginRequest
     */
    @Operation(summary = "Sign Up with User credentials")
    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signUp(final
                                 @RequestBody SignupRequest signupRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.signUp(signupRequest));
    }

    /**
     * @param registrationRequest
     * @param authHeader
     * @param principal
     * @return loginRequest
     */
    @Operation(summary = "Register the User")
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            final Principal principal,
            @RequestHeader(name = "Authorization") final String authHeader,
            final @RequestBody RegistrationRequest registrationRequest) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                authService.register(authHeader,
                        principal.getName(),
                        registrationRequest));
    }

    /**
     * signin an signin.
     * @param loginRequest
     * @return  loginRequest
     */
    @Operation(summary = "Sign in with User credentials")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(final
                               @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    /**
     * performs the login function.
     * @param authHeader
     * @param refreshToken the authentication request
     * @param principal
     * @return authentication response
     */
    @Operation(summary = "Refresh the credentials")
    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(
            final Principal principal,
            @RequestHeader(name = "Authorization") final String authHeader,
            final @RequestBody
            RefreshToken
                    refreshToken) {
        return ResponseEntity.ok().body(authService
                .refresh(authHeader,
                        principal.getName(), refreshToken));
    }

    /**
     * Logout.
     * @param authHeader
     * @return loginRequest
     */
    @Operation(summary = "Logout the User")
    @PostMapping("/logout")
    public ResponseEntity logout(
            @RequestHeader(name = "Authorization")
            final String authHeader) {
        authService.logout(authHeader);
        return ResponseEntity.ok().build();
    }

    /**
     * get the user details from the principal.
     *
     * @param principal the principal
     * @return AuthenticationResponse response entity
     */
    @Operation(summary = "Get logged in user profile",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "practice"),
            @ApiResponse(responseCode = "401",
                    description = "invalid credentials"),
            @ApiResponse(responseCode = "404",
                    description = "practice not found")})
    @GetMapping("/me")
    public ResponseEntity<UserDetails> me(
            final Principal principal) {
        return ResponseEntity.ok().body(
                authService.me(principal.getName()));
    }

}
