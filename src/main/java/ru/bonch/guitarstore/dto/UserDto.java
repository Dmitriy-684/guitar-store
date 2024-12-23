package ru.bonch.guitarstore.dto;

import lombok.Builder;
import ru.bonch.guitarstore.enums.UserRole;

@Builder
public record UserDto (

    String userId,

    String username,

    UserRole role,

    String phone,

    String fullName
) {}
