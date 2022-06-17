package com.example.security.jwt;

import com.example.security.model.UserDetailsImpl;
import com.example.service.UserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    /**
     * Logger.
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(JwtUtils.class);

    /**
     * UserDetailsService.
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * JWT Secret.
     */
    @Value("${app.auth.tokenSecret}")
    private String jwtSecret;

    /**
     * JWT Expiration.
     */
    @Value("${app.auth.tokenExpirationMsec}")
    private int jwtExpirationMs;

    /**
     * Generate JWT Token.
     * @param authentication
     * @return jwtToken
     */
    public String generateJwtToken(final Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl)
                                authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date())
                        .getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
    /**
     * Gets Authentication from token.
     * @param token
     * @return authentication
     */
    public UsernamePasswordAuthenticationToken getAuthentication(
            final String token) {
        if (token != null && validateJwtToken(token)) {
            String username = getUserNameFromJwtToken(token);

            UserDetails userDetails = userDetailsService
                    .loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
            return authentication;
        }
        return null;
    }

    /**
     * Get User Name from token.
     * @param token
     * @return userName
     */
    private String getUserNameFromJwtToken(final String token) {
        return Jwts.parser().setSigningKey(jwtSecret)
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * validate JWT.
     * @param authToken
     * @return isValidJWTToken
     */
    private boolean validateJwtToken(final String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            LOGGER.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
