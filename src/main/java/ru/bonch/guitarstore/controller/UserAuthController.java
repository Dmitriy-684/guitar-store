package ru.bonch.guitarstore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bonch.guitarstore.controller.converter.UserConverter;
import ru.bonch.guitarstore.dto.request.SigninRequest;
import ru.bonch.guitarstore.dto.request.SignupRequest;
import ru.bonch.guitarstore.dto.response.SigninResponse;
import ru.bonch.guitarstore.security.JwtUtil;
import ru.bonch.guitarstore.service.interfaces.UserService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        if (userService.isUserExistsWithUsername(request.username())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пользователь с таким логином уже существует");
        }
        var userDto = userService.save(request);
        return ResponseEntity.ok(UserConverter.fromDto(userDto));
    }

    @PostMapping("/signin")
    ResponseEntity<?> signin(@RequestBody SigninRequest request) {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            var tokenDto = jwtUtil.generateToken(authentication);
            var userDto = userService.getByUsername(request.username());
            return ResponseEntity.ok(UserConverter.fromDtoWithToken(userDto, tokenDto));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный логин или пароль");
        }
    }
}
