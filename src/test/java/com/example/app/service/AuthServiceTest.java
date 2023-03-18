package com.example.app.service;

import com.example.core.payload.LoginRequest;
import com.example.core.payload.SignupRequest;
import com.example.core.payload.JwtResponse;
import com.example.core.service.AuthService;
import org.example.MovieManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.sql.SQLException;
import java.util.Set;

@SpringBootTest
class AuthServiceTest {

}