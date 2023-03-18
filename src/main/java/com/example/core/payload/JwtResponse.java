package com.example.core.payload;

import java.util.List;

/**
 * JWT Response.
 */
public class JwtResponse {

    /**
     * token.
      */
    private String token;
    /**
     * type.
     */
    private String type = "Bearer";

    /**
     * username.
     */
    private String username;

    /**
     * roles.
     */
    private List<String> roles;
    /**
     * @param accessToken
     * @param theusername
     * @param theroles
     */
    public JwtResponse(final String accessToken,
                       final String theusername,
                       final List<String> theroles) {
        this.token = accessToken;
        this.username = theusername;
        this.roles = theroles;
    }

    /**
     * getAccessToken.
     * @return token
     */
    public String getAccessToken() {
        return token;
    }

    /**
     * setAccessToken.
     * @param accessToken
     */
    public void setAccessToken(final String accessToken) {
        this.token = accessToken;
    }

    /**
     * getTokentype.
     * @return type
     */
    public String getTokenType() {
        return type;
    }
    /**
     * setTokentype.
     * @param tokenType
     */
    public void setTokenType(final String tokenType) {
        this.type = tokenType;
    }


    /**
     * getUsername.
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * setUsername.
     * @param theusername
     */
    public void setUsername(final String theusername) {
        this.username = theusername;
    }

    /**
     * getRoles.
     * @return getRoles
     */
    public List<String> getRoles() {
        return roles;
    }
}
