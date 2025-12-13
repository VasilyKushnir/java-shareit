package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.CreateUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto create(CreateUserRequest request);

    UserDto read(Long id);

    UserDto update(Long id, UpdateUserRequest request);

    void delete(Long id);

    boolean isUserExist(Long id);
}
