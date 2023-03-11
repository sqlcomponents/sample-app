package com.example.security.jwt;

import com.example.payload.JwtResponse;
import com.example.payload.LoginRequest;
import com.example.security.model.UserDetailsImpl;
import com.example.security.UserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
     * authenticationManager.
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Cache Managemer.
     */
    @Autowired
    private CacheManager cacheManager;

    /**
     * Cache to hold auth tokens.
     */
    private Cache authCache;

    /**
     * Initialize Cache.
     */
    @PostConstruct
    public void init() {
        this.authCache = cacheManager.getCache("Auth");
    }

    /**
     * Authenticate the login request.
     * @param loginRequest
     * @return jwtResponse
     */
    public JwtResponse authenticate(final LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = generateJwtToken(authentication);

        String token = UUID.randomUUID().toString();

        this.authCache.put(token, jwt);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication
                .getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(token,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
    }

    /**
     * Generate JWT Token.
     *
     * @param authentication
     * @return jwtToken
     */
    private String generateJwtToken(final Authentication authentication) {

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
     * Gets Authentication from jwt.
     *
     * @param token
     * @return authentication
     */
    public UsernamePasswordAuthenticationToken getAuthentication(
            final String token) {

        final String jwt = getJWTToken(token);
        if (jwt != null && validateJwtToken(jwt)) {
            String username = getUserNameFromJwtToken(jwt);

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
     * Get User Name from jwtToken.
     *
     * @param jwtToken
     * @return userName
     */
    private String getUserNameFromJwtToken(final String jwtToken) {
        return Jwts.parser().setSigningKey(jwtSecret)
                .parseClaimsJws(jwtToken).getBody().getSubject();
    }

    /**
     * validate JWT.
     *
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

    /**
     * Logs Out user.
     *
     * @param token
     */
    public void logout(final String token) {
        authCache.evict(token);
    }

    /**
     * Get User Details.
     *
     * @param token
     * @return userDetails
     */
    public UserDetails me(final String token) {
         return this.userDetailsService
                 .loadUserByUsername(
                         getUserNameFromJwtToken(
                         getJWTToken(token)));
    }

    /**
     * Gets JWT Token.
     *
     * @param token
     * @return jwtToken
     */
    private String getJWTToken(final String token) {
        if (token != null) {
            Cache.ValueWrapper valueWrapper = authCache.get(token);
            if (valueWrapper != null) {
                return (String) valueWrapper.get();
            }
        }
        return null;
    }

}
