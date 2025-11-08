package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class CreateUserRequest {
    @NotBlank(message = "Name must not be blank")
    String name;

    @NotNull
    @Email(message = "Invalid email format")
    String email;
}
