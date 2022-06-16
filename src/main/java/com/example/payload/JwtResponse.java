package com.example.payload;

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
     * id.
     */
    private Long id;
    /**
     * username.
     */
    private String username;
    /**
     * email.
     */
    private String email;
    /**
     * roles.
     */
    private List<String> roles;
    /**
     * @param accessToken
     * @param theId
     * @param theusername
     * @param theemail
     * @param theroles
     */
    public JwtResponse(final String accessToken, final Long theId,
                       final String theusername, final String theemail,
                       final List<String> theroles) {
        this.token = accessToken;
        this.id = theId;
        this.username = theusername;
        this.email = theemail;
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
     * getId.
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * setId.
     * @param aid
     */
    public void setId(final Long aid) {
        this.id = aid;
    }

    /**
     * getEmail.
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * setEmail.
     * @param aemail
     */
    public void setEmail(final String aemail) {
        this.email = aemail;
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
