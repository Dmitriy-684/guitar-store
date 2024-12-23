package ru.bonch.guitarstore;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.bonch.guitarstore.dto.UserDto;
import ru.bonch.guitarstore.dto.request.SignupRequest;
import ru.bonch.guitarstore.entity.UserEntity;
import ru.bonch.guitarstore.enums.UserRole;
import ru.bonch.guitarstore.repository.UserRepository;
import ru.bonch.guitarstore.service.impl.DefaultUserService;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DefaultUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DefaultUserService userService;

    @Test
     void isUserExistsWithUsername_ShouldReturnTrue_WhenUserExists() {
        // Arrange
        String username = "testuser";
        Mockito.when(userRepository.existsByUsername(username)).thenReturn(true);

        // Act
        boolean exists = userService.isUserExistsWithUsername(username);

        // Assert
        Assertions.assertTrue(exists);
        Mockito.verify(userRepository, Mockito.times(1)).existsByUsername(username);
    }

    @Test
    void save_ShouldSaveUserAndReturnDto() {
        // Arrange
        SignupRequest request = new SignupRequest("testuser", "password123", "1234567890", "Test User");
        String hashedPassword = "hashed_password";

        Mockito.when(passwordEncoder.encode(request.password())).thenReturn(hashedPassword);

        // Act
        UserDto result = userService.save(request);

        // Assert
        ArgumentCaptor<UserEntity> userEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);
        Mockito.verify(userRepository, Mockito.times(1)).save(userEntityCaptor.capture());

        UserEntity capturedUserEntity = userEntityCaptor.getValue();
        Assertions.assertNotNull(capturedUserEntity.getUserId()); // Проверяем, что userId был сгенерирован
        Assertions.assertTrue(capturedUserEntity.getUserId().length() <= 32); // Проверяем длину userId
        Assertions.assertEquals(request.username(), capturedUserEntity.getUsername());
        Assertions.assertEquals(hashedPassword, capturedUserEntity.getPassword());
        Assertions.assertEquals(request.phone(), capturedUserEntity.getPhone());
        Assertions.assertEquals(request.fullName(), capturedUserEntity.getFullName());
        Assertions.assertEquals(UserRole.USER, capturedUserEntity.getRole());

        Assertions.assertEquals(
                new UserDto(
                        capturedUserEntity.getUserId(),
                        capturedUserEntity.getUsername(),
                        capturedUserEntity.getRole(),
                        capturedUserEntity.getPhone(),
                        capturedUserEntity.getFullName()
                ),
                result
        );

        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(request.password());
    }


    @Test
    void getByUsername_ShouldReturnDto_WhenUserExists() {
        // Arrange
        String username = "testuser";
        UserEntity userEntity = UserEntity.builder()
                .userId("generated-id")
                .username(username)
                .password("hashed_password")
                .phone("1234567890")
                .fullName("Test User")
                .role(UserRole.USER)
                .build();
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        // Act
        UserDto result = userService.getByUsername(username);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(username, result.username());
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
    }

    @Test
    void getEntityByUserId_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        String userId = "nonexistent-id";
        Mockito.when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalStateException exception = Assertions.assertThrows(
                IllegalStateException.class,
                () -> userService.getEntityByUserId(userId)
        );
        Assertions.assertEquals("User with userId + " + userId + " not found", exception.getMessage());
        Mockito.verify(userRepository, Mockito.times(1)).findByUserId(userId);
    }

    @Test
    void isUserHasRole_ShouldReturnTrue_WhenRoleMatches() {
        // Arrange
        String username = "testuser";
        UserRole role = UserRole.USER;
        UserEntity userEntity = UserEntity.builder()
                .userId("generated-id")
                .username(username)
                .password("hashed_password")
                .phone("1234567890")
                .fullName("Test User")
                .role(role)
                .build();
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        // Act
        boolean result = userService.isUserHasRole(username, role);

        // Assert
        Assertions.assertTrue(result);
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
    }
}

