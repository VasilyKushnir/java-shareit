package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dal.ItemRequestRepository;
import ru.practicum.shareit.request.dto.CreateItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoExtended;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequestDto create(Long requestorId, CreateItemRequest request) {
        User requestor = userRepository.findById(requestorId)
                .orElseThrow(() ->
                        new NotFoundException("User with id " + requestorId + " was not found"));
        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(request, requestor);
        itemRequest = itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.mapToItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDtoExtended> getMyRequests(Long requestorId) {
        List<ItemRequest> itemRequestList = itemRequestRepository.findByRequestorIdOrderByCreated(requestorId);
        List<Long> itemIdList = itemRequestList.stream()
                .map(ItemRequest::getId)
                .toList();
        List<Item> itemList = itemRepository.findByRequestIdIn(itemIdList);
        return itemRequestList.stream()
                .map(r -> {
                    List<Item> itemListForRequest = itemList.stream()
                            .filter(i -> i.getRequestId().equals(r.getId()))
                            .toList();
                    return ItemRequestMapper.mapToItemRequestDtoExtended(r, itemListForRequest);
                })
                .toList();
    }

    @Override
    public List<ItemRequestDto> getAllRequests() {
        return itemRequestRepository.findByOrderByCreated()
                .stream()
                .map(ItemRequestMapper::mapToItemRequestDto)
                .toList();
    }

    @Override
    public ItemRequestDtoExtended getSpecificRequest(Long requestId) {
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() ->
                        new NotFoundException("Request with id " + requestId + " was not found"));
        List<Item> itemList = itemRepository.findByRequestId(itemRequest.getId());
        return ItemRequestMapper.mapToItemRequestDtoExtended(itemRequest, itemList);
    }
}
