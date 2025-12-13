package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class ItemRequestDtoExtendedDetails {
    Long itemId;
    String name;
    Long itemOwnerId;
}
