package com.example.security.jwt;

import com.example.payload.JwtResponse;
import com.example.payload.LoginRequest;
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
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
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
     * value.
     */
    private final int value = 7;

    @Autowired
    private CacheManager cacheManager;

    private Cache authCache;

    @PostConstruct
    public void init(){
        this.authCache = cacheManager.getCache("Auth");
    }

    public JwtResponse authenticate(final LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = generateJwtToken(authentication);

        String token = UUID.randomUUID().toString();

        this.authCache.put(token,jwt);

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
     * @param token
     * @return authentication
     */
    public UsernamePasswordAuthenticationToken getAuthentication(
            final String token) {
        if (token != null) {
            Cache.ValueWrapper valueWrapper = authCache.get(token);
            if (valueWrapper != null) {
                final String jwt = (String) valueWrapper.get();
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
            }
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

    public void logout(final HttpServletRequest reques) {
        authCache.evict(getToken(reques));
    }

    /**
     * Gets Token from HttpRequest.
     * @param request
     * @return token
     */
    public String getToken(final HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth
                .startsWith("Bearer ")) {
            return headerAuth.substring(value, headerAuth.length());
        }

        return null;
    }
}
