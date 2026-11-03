package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.CreateItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoExtended;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                 @RequestBody CreateItemRequest request) {
        return itemRequestService.create(requestorId, request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDtoExtended> getMyRequests(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        return itemRequestService.getMyRequests(requestorId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDto> getAllRequests() {
        return itemRequestService.getAllRequests();
    }

    @GetMapping("{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDtoExtended getSpecificRequest(@PathVariable Long requestId) {
        return itemRequestService.getSpecificRequest(requestId);
    }
}
