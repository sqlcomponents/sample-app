package com.example.core.security.jwt;
import com.example.core.security.controllers.util.HttpUtil;
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
     * JwtUtils.
     */
    private final JwtUtils jwtUtils;

    /**
     * Builds AuthTokenFilter.
     * @param aJwtUtils
     */
    public AuthTokenFilter(final JwtUtils aJwtUtils) {
        this.jwtUtils = aJwtUtils;
    }


    /**
     * @param request an HttpServletRequest.
     * @param response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain)
            throws ServletException, IOException {
        final String token = HttpUtil.getToken(request);
        try {
            UsernamePasswordAuthenticationToken authentication =
                    jwtUtils.getAuthentication(token);
            if (authentication != null) {
                authentication.setDetails(new WebAuthenticationDetailsSource()
                                           .buildDetails(request));
                SecurityContextHolder.getContext()
                                    .setAuthentication(authentication);
            }
        } catch (Exception e) {
            LOGGER.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }


}
