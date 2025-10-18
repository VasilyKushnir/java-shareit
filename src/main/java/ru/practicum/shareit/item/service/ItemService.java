package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CreateItemRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.List;

public interface ItemService {
    ItemDto create(Long id, CreateItemRequest request);

    ItemDto read(Long id);

    List<ItemDto> readUserItems(Long id);

    List<ItemDto> searchItems(Long id, String text);

    ItemDto update(Long ownerId, Long itemId, UpdateItemRequest request);
}
