package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemDtoExtended {
    Long id;
    String name;
    String description;
    Boolean available;
    LocalDateTime lastBooking;
    LocalDateTime nextBooking;
    List<CommentDto> comments;
}
