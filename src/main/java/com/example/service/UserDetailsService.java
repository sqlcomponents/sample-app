package com.example.service;

import com.example.security.model.UserDetailsImpl;
import org.example.MovieManager;
import org.example.model.UserRoles;
import org.example.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.store.UserRolesStore.userId;

@Service
public class UserDetailsService implements org.springframework.security
                                    .core.userdetails.UserDetailsService {
    /**
     * MovieManager.
     */
    @Autowired
    private MovieManager movieManager;

    /**
     * @param username a username.
     * @return null
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username)
                                  throws UsernameNotFoundException {
        Users user = null;
        try {
            user = movieManager.getUsersStore().selectByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            "User Not Found with username: " + username));
            return build(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private UserDetailsImpl build(final Users user) throws SQLException {

        List<UserRoles> userRoles = movieManager
                .getUserRolesStore()
                .select(userId().eq(user.getId())).execute();

        List<GrantedAuthority> authorities = userRoles.stream()
                .map(userRole -> {
                    try {
                        return new SimpleGrantedAuthority(movieManager
                                .getRolesStore()
                                .select(userRole.getRoleId()).get().getName());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

}