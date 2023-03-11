package com.example.payload;

import jakarta.validation.constraints.NotBlank;

/**
 * Login Request.
 */
public class LoginRequest {
    /**
     * User Name.
     */
    @NotBlank
    private String username;

    /**
     * Password.
     */
    @NotBlank
    private String password;

    /**
     * Gets User Name.
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets User Name.
     * @param aUsername
     */
    public void setUsername(final String aUsername) {
        this.username = aUsername;
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
