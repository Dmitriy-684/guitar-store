package ru.bonch.guitarstore.controller.converter;

import ru.bonch.guitarstore.dto.TokenDto;
import ru.bonch.guitarstore.dto.UserDto;
import ru.bonch.guitarstore.dto.response.SigninResponse;
import ru.bonch.guitarstore.dto.response.SignupResponse;

public class UserConverter {

    public static SignupResponse fromDto(UserDto userDto) {
        return SignupResponse.builder()
                .username(userDto.username())
                .userId(userDto.userId())
                .fullName(userDto.fullName())
                .phone(userDto.phone())
                .build();
    }

    public static SigninResponse fromDtoWithToken(UserDto userDto, TokenDto tokenDto) {
        return SigninResponse.builder()
                .userId(userDto.userId())
                .role(userDto.role().name())
                .token(tokenDto.token())
                .build();
    }
}
