package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@AllArgsConstructor
@Value
@Builder
public class CreateCommentRequest {
    String text;
}
