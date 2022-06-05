package com.example.security.service;

import org.example.MovieManager;
import org.example.model.UserRoles;
import org.example.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.store.UserRolesStore.userId;
import static org.example.store.UsersStore.username;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    MovieManager movieManager;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = null;
        try {
            user = movieManager.getUsersStore().select(username().eq(username)).stream().findFirst()
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
            return build(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private UserDetailsImpl build(Users user) throws SQLException {

        List<UserRoles> userRoles = movieManager
                .getUserRolesStore()
                .select(userId().eq(user.getId()));

        List<GrantedAuthority> authorities = userRoles.stream()
                .map(userRole -> {
                    try {
                        return new SimpleGrantedAuthority(movieManager
                                .getRolesStore()
                                .find(userRole.getRoleId()).getName());
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
