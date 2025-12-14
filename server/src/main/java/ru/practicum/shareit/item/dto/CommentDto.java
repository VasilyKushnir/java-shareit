package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@AllArgsConstructor
@Builder(toBuilder = true)
public class CommentDto {
    Long id;
    String text;
    String authorName;
    LocalDateTime created;
}
