package com.example.service;

import com.example.payload.LoginRequest;
import com.example.payload.SignupRequest;
import com.example.payload.JwtResponse;
import com.example.payload.MessageResponse;
import com.example.security.jwt.JwtUtils;
import org.example.MovieManager;
import org.example.model.UserRoles;
import org.example.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public JwtResponse authenticate(final LoginRequest loginRequest) {
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

    /**
     * @param signUpRequest an signUpRequest.
     * @return MessageResponse
     */
    public MessageResponse register(final SignupRequest signUpRequest)
                                                 throws SQLException {
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

        final Users createduser = movieManager.getUsersStore().insert()
                                           .values(user).returning();

        Set<String> roles = signUpRequest.getRole() == null
                ? Set.of("ROLE_USER") : signUpRequest.getRole();
        List<UserRoles> userRoles = roles
                                        .stream().map(sRole -> {
            UserRoles userRoles1 = new UserRoles();
            userRoles1.setUserId(createduser.getId());
            try {
                userRoles1.setRoleId(movieManager.getRolesStore()
                             .selectByName(sRole).get().getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return userRoles1;
        }).collect(Collectors.toList());
        movieManager.getUserRolesStore().insert().values(userRoles).execute();

        return new MessageResponse("User registered successfully!");
    }
}
