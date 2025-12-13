package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.CreateItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoExtended;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(Long requestorId, CreateItemRequest request);

    List<ItemRequestDtoExtended> getMyRequests(Long requestorId);

    List<ItemRequestDto> getAllRequests();

    ItemRequestDtoExtended getSpecificRequest(Long requestId);
}
