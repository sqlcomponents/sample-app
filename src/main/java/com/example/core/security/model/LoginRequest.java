package com.example.core.security.model;

import jakarta.validation.constraints.NotBlank;

/**
 * Login Request.
 */
public class LoginRequest {
    /**
     * User Name.
     */
    @NotBlank
    private String userName;

    /**
     * Password.
     */
    @NotBlank
    private String password;

    /**
     * Gets User Name.
     * @return username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets User Name.
     * @param aUsername
     */
    public void setUserName(final String aUsername) {
        this.userName = aUsername;
    }

    /**
     * Get Password.
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set Password.
     * @param thePassword
     */
    public void setPassword(final String thePassword) {
        this.password = thePassword;
    }
}
