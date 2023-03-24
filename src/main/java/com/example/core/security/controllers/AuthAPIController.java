package com.example.core.security.controllers;

import com.example.core.payload.SignupRequest;
import com.example.core.security.controllers.util.HttpUtil;
import com.example.core.payload.JwtResponse;
import com.example.core.payload.LoginRequest;
import com.example.core.security.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

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
    public ResponseEntity<JwtResponse> signUp(final
                                 @RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok(authService.signUp(signupRequest));
    }

    /**
     * signin an signin.
     * @param loginRequest
     * @return  loginRequest
     */
    @Operation(summary = "Sign in with User credentials")
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(final
                               @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    /**
     * Logout.
     * @param request
     * @return loginRequest
     */
    @Operation(summary = "Logout the User")
    @PostMapping("/logout")
    public ResponseEntity logout(final HttpServletRequest request) {
        authService.logout(HttpUtil.getToken(request));
        return ResponseEntity.ok().build();
    }

    /**
     * Get User Details.
     * @param request
     * @return loginRequest
     */
    @Operation(summary = "Logout the User")
    @GetMapping("/me")
    public ResponseEntity<UserDetails> me(final HttpServletRequest request) {
        return ResponseEntity.ok(authService.me(HttpUtil.getToken(request)));
    }
}
