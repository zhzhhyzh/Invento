package com.zheheng.InventoryManagementSpringboot.security;

import com.zheheng.InventoryManagementSpringboot.exceptions.NotFoundException;
import com.zheheng.InventoryManagementSpringboot.models.User;
import com.zheheng.InventoryManagementSpringboot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new NotFoundException("User Email not found"));

        return AuthUser.builder()
                .user(user)
                .build();
    }
}
