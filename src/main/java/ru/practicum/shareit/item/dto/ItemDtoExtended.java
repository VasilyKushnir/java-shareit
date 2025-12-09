package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
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
