package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.RequestClient;
import ru.practicum.shareit.request.dto.CreateItemRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                         @RequestBody @Valid CreateItemRequest request) {
        return requestClient.create(requestorId, request);
    }

    @GetMapping
    public ResponseEntity<Object> getMyRequests(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        return requestClient.getMyRequests(requestorId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests() {
        return requestClient.getAllRequests();
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> getSpecificRequest(@PathVariable Long requestId) {
        return requestClient.getSpecificRequest(requestId);
    }
}
