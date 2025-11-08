package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static Item mapToItem(Long id, CreateItemRequest request) {
        return Item.builder()
                .name(request.getName())
                .description(request.getDescription())
                .available(request.getAvailable())
                .owner(id)
                .build();
    }

    public static ItemDto mapToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static Item updateItem(Item item, UpdateItemRequest request) {
        Item.ItemBuilder builder = item.toBuilder();
        if (request.hasName()) {
            builder.name(request.getName());
        }
        if (request.hasDescription()) {
            builder.description(request.getDescription());
        }
        if (request.hasAvailableStatus()) {
            builder.available(request.getAvailable());
        }
        return builder.build();
    }
}
