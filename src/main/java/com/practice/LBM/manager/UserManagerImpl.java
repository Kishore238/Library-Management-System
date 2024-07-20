package com.practice.LBM.manager;

import com.practice.LBM.entity.Fine;
import com.practice.LBM.entity.User;
import com.practice.LBM.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

public class UserManagerImpl implements UserManager, UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void createUser(String username, String password, String role) {
        Fine fine = Fine.builder()
                .amount(0)
                .build();
        User user = User.builder()
                .username(username)
                .fine(fine)
                .password(passwordEncoder.encode(password))
                .role(role)
                .build();
        userRepository.save(user);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new UsernameNotFoundException("username not found")
        );
        org.springframework.security.core.userdetails.UserDetails springUser = org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
        return springUser;
    }
}
