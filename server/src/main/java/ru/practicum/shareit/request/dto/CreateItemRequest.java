package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@AllArgsConstructor
@Value
@Builder
public class CreateItemRequest {
    String description;
}
