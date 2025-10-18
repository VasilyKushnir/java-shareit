package ru.practicum.shareit.item.dal;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item create(Item item);

    Optional<Item> read(Long id);

    List<Item> readUserItems(Long id);

    List<Item> searchItems(Long id, String text);

    Item update(Item item);
}
