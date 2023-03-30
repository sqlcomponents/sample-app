package com.example.core.security.config;

import com.example.core.security.service.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AuthTokenFilter.class);
    /**
     * TokenProvider.
     */
    private final TokenProvider tokenProvider;

    /**
     * Builds AuthTokenFilter.
     *
     * @param aJwtUtils
     */
    public AuthTokenFilter(final TokenProvider aJwtUtils) {
        this.tokenProvider = aJwtUtils;
    }


    /**
     * override method to.
     *
     * @param request     request
     * @param response    response
     * @param filterChain filterchain
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain)
            throws ServletException, IOException {

        UsernamePasswordAuthenticationToken authentication =
                tokenProvider.getAuthentication(
                        request.getRequestURI(),
                        request.getHeader("Authorization"));
        authentication.setDetails(
                new WebAuthenticationDetailsSource()
                .buildDetails(request));
        SecurityContextHolder.getContext()
                .setAuthentication(
                        authentication);

        filterChain.doFilter(request, response);
    }


}
