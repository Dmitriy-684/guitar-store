package ru.bonch.guitarstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.bonch.guitarstore.repository.UserRepository;
import ru.bonch.guitarstore.security.DefaultUserDetails;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
        return DefaultUserDetails.builder()
                .username(username)
                .password(user.getPassword())
                .roles(List.of(user.getRole()))
                .build();
    }
}
