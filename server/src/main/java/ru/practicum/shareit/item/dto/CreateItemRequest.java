package ru.practicum.shareit.item.dto;

import lombok.Value;

@Value
public class CreateItemRequest {
    String name;

    String description;

    Boolean available;

    Long requestId;
}
