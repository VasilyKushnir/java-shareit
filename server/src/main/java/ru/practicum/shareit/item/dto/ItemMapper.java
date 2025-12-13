package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

public class ItemMapper {
    public static Item mapToItem(CreateItemRequest request) {
        Item item = new Item();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setAvailable(request.getAvailable());
        item.setRequestId(request.getRequestId());
        return item;
    }

    public static ItemDto mapToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static ItemDtoExtended mapToItemDtoExtended(Item item,
                                                       LocalDateTime lastBooking,
                                                       LocalDateTime nextBooking,
                                                       List<Comment> comments) {
        return ItemDtoExtended.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(
                        comments.stream()
                        .map(CommentMapper::mapToCommentDto)
                        .toList()
                )
                .build();
    }

    public static Item updateItem(Item item, UpdateItemRequest request) {
        if (request.hasName()) {
            item.setName(request.getName());
        }
        if (request.hasDescription()) {
            item.setDescription(request.getDescription());
        }
        if (request.hasAvailableStatus()) {
            item.setAvailable(request.getAvailable());
        }
        return item;
    }
}
