package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

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

    public static ItemRequestDtoExtended mapToItemRequestDtoExtended(ItemRequest request, List<Item> itemList) {
        List<ItemRequestDtoExtendedDetails> detailsList = itemList.stream()
                .map(i -> {
                    return ItemRequestDtoExtendedDetails.builder()
                            .itemId(i.getId())
                            .name(i.getName())
                            .itemOwnerId(i.getOwner().getId())
                            .build();
                })
                .toList();

        return ItemRequestDtoExtended.builder()
                .id(request.getId())
                .description(request.getDescription())
                .requestor(UserMapper.mapToUserDto(request.getRequestor()))
                .created(request.getCreated())
                .items(detailsList)
                .build();
    }
}
