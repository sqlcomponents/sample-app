package com.example.security.service;

import com.example.payload.request.LoginRequest;
import com.example.payload.request.SignupRequest;
import com.example.payload.response.JwtResponse;
import com.example.payload.response.MessageResponse;
import com.example.security.jwt.JwtUtils;
import com.example.security.model.UserDetailsImpl;
import org.example.MovieManager;
import org.example.model.UserRoles;
import org.example.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    MovieManager movieManager;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    public JwtResponse authenticate(final LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
    }

    public MessageResponse register(final SignupRequest signUpRequest) throws SQLException {
//        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
//
//        }
//
//        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//
//        }

        // Create new user's account
        Users user = new Users();

        user.setEmail(signUpRequest.getEmail());
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));

        final Users createduser = movieManager.getUsersStore().insert().values(user).returning();


        List<UserRoles> userRoles = signUpRequest.getRole().stream().map(sRole -> {
            UserRoles userRoles1 = new UserRoles();
            userRoles1.setUserId(createduser.getId());
            try {
                userRoles1.setRoleId(movieManager.getRolesStore().selectByName(sRole).get().getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return userRoles1;
        }).collect(Collectors.toList());
        movieManager.getUserRolesStore().insert().values(userRoles).execute();

        return new MessageResponse("User registered successfully!");
    }
}
