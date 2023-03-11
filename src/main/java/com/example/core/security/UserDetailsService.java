package com.example.core.security;

import com.example.core.security.model.UserDetailsImpl;
import org.example.MovieManager;
import org.example.model.UserRoles;
import org.example.model.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.example.store.UserRolesStore.userId;

@Service
public class UserDetailsService implements org.springframework.security
                                    .core.userdetails.UserDetailsService {
    /**
     * MovieManager.
     */
    private final MovieManager movieManager;

    /**
     * Bilds Movie Manager.
     * @param theMovieManager
     */
    public UserDetailsService(final MovieManager theMovieManager) {
        this.movieManager = theMovieManager;
    }

    /**
     * @param username a username.
     * @return null
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username)
                                  throws UsernameNotFoundException {
        try {
            return build(movieManager.getUsersStore().selectByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            "User Not Found with username: " + username)));
        } catch (SQLException e) {
            throw new UsernameNotFoundException(
                    "User Not Found with username: " + username, e);
        }
    }

    private UserDetails build(final Users user) throws SQLException {

        List<UserRoles> userRoles = movieManager
                .getUserRolesStore()
                .select(userId().eq(user.getId())).execute();

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (UserRoles userRole : userRoles) {
            SimpleGrantedAuthority grantedAuthority
                    = getGrantedAuthority(userRole);
            authorities.add(grantedAuthority);
        }

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

    /**
     * Get authority from a Role.
     * @param userRole
     * @return authority
     */
    private SimpleGrantedAuthority getGrantedAuthority(
            final UserRoles userRole) throws SQLException {

            return new SimpleGrantedAuthority(movieManager
                    .getRolesStore()
                    .select(userRole.getRoleId()).get().getName());

    }

}
