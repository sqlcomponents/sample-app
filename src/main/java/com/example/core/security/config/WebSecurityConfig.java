package com.example.core.security.config;

import com.example.core.security.service.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Web Security Configuration.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
public class WebSecurityConfig {




    /**
     * PasswordEncoder.
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Builds AuthenticationManager.
     * @param config
     * @return authenticationManager
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(
            final AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * HttpSecurity.
     * @param jwtUtils
     * @param authEntryPointJwt
     * @param http
     * @return SecurityFilterChain
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(
            final HttpSecurity http,
            final AuthEntryPointJwt authEntryPointJwt,
            final TokenProvider jwtUtils) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(
                        authEntryPointJwt).and()
                .sessionManagement().sessionCreationPolicy(
                                    SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests()
                .requestMatchers("/api/auth/signup",
                        "/api/auth/login",
                        "/api/test/**",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**")
                .permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(new AuthTokenFilter(jwtUtils),
                UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * In Memory Users.
     * @param passwordEncoder
     * @return users
     */
    @Bean
    public UserDetailsService users(final PasswordEncoder passwordEncoder) {
        // The builder will ensure the passwords
        // are encoded before saving in memory
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .roles("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }
}
