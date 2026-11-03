package ru.practicum.shareit.user.dto;

import lombok.Value;

@Value
public class CreateUserRequest {
    String name;

    String email;
}
