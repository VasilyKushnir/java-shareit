package ru.practicum.shareit.request.dal;

import ru.practicum.shareit.request.ItemRequest;

public interface ItemRequestRepository {
    ItemRequest save(ItemRequest itemRequest);
}
