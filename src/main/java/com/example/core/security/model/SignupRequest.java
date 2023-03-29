package com.example.core.security.model;

import java.util.Set;

/**
 * SignupRequest.
 */

public class SignupRequest extends LoginRequest {

    /**
     * role.
     */
    private Set<String> roles;

    /**
     * getrole.
     * @return role
     */
    public Set<String> getRoles() {
        return this.roles;
    }

    /**
     * setRole.
     * @param arole
     */
    public void setRoles(final Set<String> arole) {
        this.roles = arole;
    }
}
