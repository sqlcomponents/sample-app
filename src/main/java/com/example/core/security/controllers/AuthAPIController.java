package com.example.core.security.controllers;

import com.example.core.payload.RegistrationRequest;
import com.example.core.payload.SignupRequest;
import com.example.core.payload.AuthenticationResponse;
import com.example.core.payload.LoginRequest;
import com.example.core.security.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.ok(authService.signUp(signupRequest));
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

//    /**
//     * Get User Details.
//     * @param request
//     * @return loginRequest
//     */
//    @Operation(summary = "Logout the User")
//    @GetMapping("/me")
//    public ResponseEntity<UserDetails> me(final HttpServletRequest request) {
//        return ResponseEntity.ok(authService.me(HttpUtil.getToken(request)));
//    }
}
