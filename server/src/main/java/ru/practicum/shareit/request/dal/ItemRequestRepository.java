package ru.practicum.shareit.request.dal;

import ru.practicum.shareit.request.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface ItemRequestRepository {
    ItemRequest save(ItemRequest itemRequest);

    List<ItemRequest> findByRequestorIdOrderByCreated(Long requestorId);

    List<ItemRequest> findByOrderByCreated();

    Optional<ItemRequest> findById(Long requestId);
}
