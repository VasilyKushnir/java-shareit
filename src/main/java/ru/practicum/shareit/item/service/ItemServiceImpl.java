package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.dto.CreateItemRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public ItemDto create(Long id, CreateItemRequest request) {
        User owner = userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("User with id " + id + " was not found, so adding item is impossible"));
        Item item = ItemMapper.mapToItem(request);
        item.setOwner(owner);
        item = itemRepository.save(item);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto read(Long id) {
        return itemRepository.findById(id)
                .map(ItemMapper::mapToItemDto)
                .orElseThrow(() -> new NotFoundException("Item with id " + id + " does not exist"));
    }

    @Override
    public List<ItemDto> readUserItems(Long id) {
        return itemRepository.findByOwnerId(id).stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public List<ItemDto> searchItems(Long id, String text) {
        return itemRepository.searchItems(id, text).stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public ItemDto update(Long ownerId, Long itemId, UpdateItemRequest request) {
        Item currentItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " does not exist"));
        if (!currentItem.getOwner().getId().equals(ownerId)) {
            throw new ForbiddenException("User do not own item with id " + itemId);
        }
        Item updatedItem = ItemMapper.updateItem(currentItem, request);
        updatedItem = itemRepository.save(updatedItem);
        return ItemMapper.mapToItemDto(updatedItem);
    }
}
