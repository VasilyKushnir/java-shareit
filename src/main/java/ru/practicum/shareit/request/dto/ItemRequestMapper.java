package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserMapper;

import java.time.LocalDateTime;

public class ItemRequestMapper {
    public static ItemRequest mapToItemRequest(CreateItemRequest request, User requestor) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(request.getDescription());
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequest;
    }

    public static ItemRequestDto mapToItemRequestDto(ItemRequest request) {
        return ItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .requestor(UserMapper.mapToUserDto(request.getRequestor()))
                .created(request.getCreated())
                .build();
    }
}
