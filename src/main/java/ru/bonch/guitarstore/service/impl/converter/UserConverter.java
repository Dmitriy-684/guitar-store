package ru.bonch.guitarstore.service.impl.converter;

import ru.bonch.guitarstore.dto.UserDto;
import ru.bonch.guitarstore.entity.UserEntity;
import ru.bonch.guitarstore.enums.UserRole;

public final class UserConverter {

    public static UserDto fromEntity(UserEntity userEntity) {
        return UserDto.builder()
                .userId(userEntity.getUserId())
                .username(userEntity.getUsername())
                .role(userEntity.getRole())
                .phone(userEntity.getPhone())
                .fullName(userEntity.getFullName())
                .build();
    }
}
