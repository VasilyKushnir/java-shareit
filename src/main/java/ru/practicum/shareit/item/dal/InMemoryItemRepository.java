package ru.practicum.shareit.item.dal;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private Long id = 0L;
    private final Map<Long, Item> itemMap = new HashMap<>();

    @Override
    public Item create(Item item) {
        id++;
        itemMap.put(id, item.toBuilder().id(id).build());
        return itemMap.get(id);
    }

    @Override
    public Optional<Item> read(Long id) {
        return Optional.ofNullable(itemMap.get(id));
    }

    @Override
    public List<Item> readUserItems(Long id) {
        return itemMap.values().stream()
                .filter(item -> item.getOwner().equals(id))
                .toList();
    }

    @Override
    public List<Item> searchItems(Long id, String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return itemMap.values().stream()
                .filter(item -> item.getAvailable()
                        && item.getOwner().equals(id)
                        && item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .toList();
    }

    @Override
    public Item update(Item item) {
        itemMap.put(item.getId(), item);
        return item;
    }
}