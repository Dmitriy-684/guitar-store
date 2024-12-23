package ru.bonch.guitarstore.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.bonch.guitarstore.dto.UserDto;
import ru.bonch.guitarstore.dto.request.SignupRequest;
import ru.bonch.guitarstore.entity.UserEntity;
import ru.bonch.guitarstore.enums.UserRole;
import ru.bonch.guitarstore.repository.UserRepository;
import ru.bonch.guitarstore.service.impl.converter.UserConverter;
import ru.bonch.guitarstore.service.interfaces.UserService;

import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Boolean isUserExistsWithUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserDto save(SignupRequest request) {
        var userId = UUID.randomUUID().toString().substring(0, 32);
        var hashedPassword = passwordEncoder.encode(request.password());
        var userEntity = UserEntity.builder()
                .userId(userId)
                .username(request.username())
                .password(hashedPassword)
                .phone(request.phone())
                .fullName(request.fullName())
                .role(UserRole.USER)
                .build();

        userRepository.save(userEntity);

        return UserConverter.fromEntity(userEntity);
    }

    @Override
    public UserDto getByUsername(String username) {
        var userEntity = findByUsername(username);

        return UserConverter.fromEntity(userEntity);
    }

    @Override
    public UserEntity getEntityByUserId(String userId) {
        var userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("User with userId + " + userId + " not found"));

        return userEntity;
    }

    @Override
    public Boolean isUserHasRole(String username, UserRole role) {
        var userEntity = findByUsername(username);

        return userEntity.getRole() == role;
    }

    private UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User with username + " + username + " not found"));
    }
}
