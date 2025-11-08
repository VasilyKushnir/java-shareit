package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Value;

@Value
public class UpdateUserRequest {
    String name;

    @Email(message = "Invalid email format")
    String email;

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasEmail() {
        return !(email == null || email.isBlank());
    }
}
