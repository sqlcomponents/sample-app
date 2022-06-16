package com.example.security.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
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
     * authorities.
     */
    @JsonIgnore
    private String password;
    /**
     * apassword.
     */
    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * Builds UserDetailsImpl.
     * @param theId
     * @param anUsername
     * @param aEmail
     * @param anPassword
     * @param anAuthorities
     */
    public UserDetailsImpl(final Long theId, final String anUsername,
                                     final String aEmail,
                                     final String anPassword,
                           final Collection<? extends GrantedAuthority>
                                                 anAuthorities) {
        this.id = theId;
        this.username = anUsername;
        this.email = aEmail;
        this.password = anPassword;
        this.authorities = anAuthorities;
    }


    /**
     * Get Authorities.
     * @return authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * gets id.
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * gets email.
     * @return email
     */
    public String getEmail() {
        return email;
    }
    /**
     * get password.
     * @return password
     */
    @Override
    public String getPassword() {
        return password;
    }
    /**
     * get username.
     * @return username
     */
    @Override
    public String getUsername() {
        return username;
    }
    /**
     * is Account NonExpired.
     * @return boolean value
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    /**
     * UserDetailsService.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    /**
     * UserDetailsService.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    /**
     * UserDetailsService.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * UserDetailsService.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    /**
     * hashCode.
     * @return Objects
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, authorities);
    }
}
