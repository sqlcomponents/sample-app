package com.example.core.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
                     LoggerFactory.getLogger(AuthEntryPointJwt.class);

    /**
     * Object Mapper.
     */
    private final ObjectMapper objectMapper;

    /**
     * Builds AuthEntryPointJwt.
     * @param theObjectMapper
     */
    public AuthEntryPointJwt(final ObjectMapper theObjectMapper) {
        this.objectMapper = theObjectMapper;
    }

    /**
     * @param request
     * @param response
     * @param authException
     */
    @Override
    public void commence(final HttpServletRequest request,
                        final HttpServletResponse response,
                        final AuthenticationException authException)
            throws IOException {
        LOGGER.error("Unauthorized error: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", authException.getMessage());
        body.put("path", request.getServletPath());


        this.objectMapper.writeValue(response.getOutputStream(), body);
    }

}
