package com.example.security.jwt;
import com.example.controllers.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @Autowired
    private JwtUtils jwtUtils;
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
