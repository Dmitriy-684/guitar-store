package ru.bonch.guitarstore.service.interfaces;

import ru.bonch.guitarstore.dto.UserDto;
import ru.bonch.guitarstore.dto.request.SignupRequest;
import ru.bonch.guitarstore.entity.UserEntity;
import ru.bonch.guitarstore.enums.UserRole;

public interface UserService {

    UserDto save(SignupRequest request);

    UserDto getByUsername(String username);

    UserEntity getEntityByUserId(String userId);

    Boolean isUserHasRole(String username, UserRole role);

    Boolean isUserExistsWithUsername(String username);

}
