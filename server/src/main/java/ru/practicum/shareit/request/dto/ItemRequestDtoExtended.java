package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder(toBuilder = true)
public class ItemRequestDtoExtended {
    Long id;
    String description;
    UserDto requestor;
    LocalDateTime created;
    List<ItemRequestDtoExtendedDetails> items;
}
