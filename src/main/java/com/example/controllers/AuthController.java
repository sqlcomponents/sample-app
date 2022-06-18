package com.example.controllers;

import com.example.controllers.util.HttpUtil;
import com.example.payload.LoginRequest;
import com.example.payload.SignupRequest;
import com.example.payload.JwtResponse;
import com.example.payload.MessageResponse;
import com.example.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication",
        description = "Resource to manage authentication")
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
    @Operation(summary = "Signin to a new User")
    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(final @Valid
                               @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticate(loginRequest));
    }

    /**
     * @param signUpRequest
     * @return loginRequest
     */
    @Operation(summary = "Signup the User")
    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(final @Valid
                                     @RequestBody SignupRequest signUpRequest)
                                      throws SQLException {
        return ResponseEntity.ok(authService.register(signUpRequest));
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
