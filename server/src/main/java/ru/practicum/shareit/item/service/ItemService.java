package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {
    ItemDto create(Long id, CreateItemRequest request);

    CommentDto addComment(Long userId, Long itemId, CreateCommentRequest request);

    ItemDtoExtended read(Long userId, Long itemId);

    List<ItemDto> readUserItems(Long id);

    List<ItemDto> searchItems(Long id, String text);

    ItemDto update(Long ownerId, Long itemId, UpdateItemRequest request);
}
