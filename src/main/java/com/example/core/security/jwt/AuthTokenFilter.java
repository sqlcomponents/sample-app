package com.example.core.security.jwt;
import com.example.core.security.controllers.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
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
        try {
            final String jwt = HttpUtil.getToken(request);

            if (StringUtils.hasText(jwt)) {
                UsernamePasswordAuthenticationToken authentication =
                        tokenProvider.getAuthentication(
                                request.getRequestURI(), jwt);
                authentication.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext()
                        .setAuthentication(
                                authentication);

            }
        } catch (final Exception ex) {
            LOGGER.error(
                    "Could not set user authentication in security context",
                    ex);
        }

        filterChain.doFilter(request, response);
    }


}
