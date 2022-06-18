package com.example.controllers.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Utilities related to HTTP.
 */
public final class HttpUtil {

    /**
     * Private Constructor.
     */
    private HttpUtil() {

    }

    /**
     * value.
     */
    private static final int VALUE = 7;

    /**
     * Gets Token from HttpRequest.
     *
     * @param request
     * @return token
     */
    public static String getToken(final HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth
                .startsWith("Bearer ")) {
            return headerAuth.substring(VALUE);
        }

        return null;
    }
}
