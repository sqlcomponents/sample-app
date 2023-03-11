package com.example.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

/**
 * SignupRequest.
 */

public class SignupRequest {
    /**
     * minvalue.
     */
    private final int minvalue = 3;
    /**
     * minvalue2.
     */
    private final int minvalue2 = 6;
    /**
     * maxvalue.
     */
    private final int maxvalue = 20;
    /**
     * maxvalue2.
     */
    private final int maxvalue2 = 50;
    /**
     * maxvalue3.
     */
    private final int maxvalue3 = 40;
    /**
     * username.
     */
    @NotBlank
    @Size(min = minvalue, max = maxvalue)
    private String username;

    /**
     * email.
     */
    @NotBlank
    @Size(max = maxvalue2)
    @Email
    private String email;
    /**
     * role.
     */
    private Set<String> role;
    /**
     * password.
     */
    @NotBlank
    @Size(min = minvalue2, max = maxvalue3)
    private String password;

    /**
     * getUsername.
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * sets User Name.
     * @param ausername
     */
    public void setUsername(final String ausername) {
        this.username = ausername;
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
     * @param theemail
     */
    public void setEmail(final String theemail) {
        this.email = theemail;
    }

    /**
     * getPassword.
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * setPassword.
     * @param thepassword
     */
    public void setPassword(final String thepassword) {
        this.password = thepassword;
    }

    /**
     * getrole.
     * @return role
     */
    public Set<String> getRole() {
        return this.role;
    }

    /**
     * setRole.
     * @param arole
     */
    public void setRole(final Set<String> arole) {
        this.role = arole;
    }
}
