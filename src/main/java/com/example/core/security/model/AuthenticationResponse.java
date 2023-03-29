package com.example.core.security.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Authentication response.
 */
public final class AuthenticationResponse {
    /**
     * declares variable userName.
     */
    private final String userName;
    /**
     * declares variable accessToken.
     */
    private final String accessToken;

    /**
     * declares variable expiresIn.
     */
    private final Long expiresIn;

    /**
     * declares variable refreshToken.
     */
    private final String refreshToken;

    /**
     * declares variable registrationToken.
     */
    private final String registrationToken;

    /**
     * initializes the value for accessToken,refresh_token,profile_pic.
     *
     * @param anUserName      the an user name
     * @param anAccessToken     the an auth token
     * @param anExpiresIn       the anExpiresIn
     * @param aRefreshToken   the a refresh token
     * @param aRegistrationToken the a registration token
     */
    @JsonCreator
    public AuthenticationResponse(
        @JsonProperty("userName") final String anUserName,
        @JsonProperty("accessToken") final String anAccessToken,
        @JsonProperty("expires_in") final Long anExpiresIn,
        @JsonProperty("refresh_token") final String aRefreshToken,
        @JsonProperty("registration_token") final String aRegistrationToken) {
        this.userName = anUserName;
        this.accessToken = anAccessToken;
        this.expiresIn = anExpiresIn;
        this.refreshToken = aRefreshToken;
        this.registrationToken = aRegistrationToken;
    }
    /**
     * gets the value for expiresIn.
     *
     * @return expiresIn
     */
    public Long getExpiresIn() {
        return expiresIn;
    }

    /**
     * gets the value for auth token.
     *
     * @return auth token
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * gets the value for registration token.
     *
     * @return registration token
     */
    public String getRegistrationToken() {
        return registrationToken;
    }

    /**
     * gets the value for refresh token.
     *
     * @return refresh token
     */
    public String getRefreshToken() {
        return refreshToken;
    }


    /**
     * gets the value for userName.
     *
     * @return userName user name
     */
    public String getUserName() {
        return userName;
    }
}
