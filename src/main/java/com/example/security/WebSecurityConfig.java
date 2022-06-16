package com.example.security;

import com.example.security.jwt.AuthEntryPointJwt;
import com.example.security.jwt.AuthTokenFilter;
import com.example.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Web Security Configuration.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * User Details Service.
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * AuthEntryPointJwt.
     */
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    /**
     * authenticationJwtTokenFilter.
     * @return AuthTokenFilter
     */
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
                                        return new AuthTokenFilter();
    }

    /**
     * AuthenticationManagerBuilder.
     * @param authenticationManagerBuilder
     * @throws Exception
     */
    @Override
    public void configure(final AuthenticationManagerBuilder
                                      authenticationManagerBuilder)
                                                    throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                                 .passwordEncoder(passwordEncoder());
    }

    /**
     * AuthenticationManager.
     * @return authenticationManagerBean
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * PasswordEncoder.
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * HttpSecurity.
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(
                                               unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(
                                    SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/api/auth/**")
                .permitAll()
                .antMatchers("/api/test/**").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(),
                UsernamePasswordAuthenticationFilter.class);
    }
}
