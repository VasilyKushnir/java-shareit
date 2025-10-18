package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class CreateItemRequest {
    @NotBlank(message = "Name must not be blank")
    String name;

    @NotBlank(message = "Description must not be blank")
    String description;

    @NotNull(message = "Status must be presented")
    Boolean available;
}
