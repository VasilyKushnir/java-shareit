package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CreateCommentRequest;
import ru.practicum.shareit.item.dto.CreateItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestBody @Valid CreateItemRequest request) {
        return itemClient.create(userId, request);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable("itemId") Long itemId,
                                             @RequestBody @Valid CreateCommentRequest request) {
        return itemClient.addComment(userId, itemId, request);
    }

    @GetMapping(path = "/{itemId}")
    public ResponseEntity<Object> read(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @PathVariable Long itemId) {
        return itemClient.read(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> readUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.readUserItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam String text) {
        return itemClient.searchItems(userId, text);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                         @PathVariable("id") Long itemId,
                                         @RequestBody UpdateItemRequest request) {
        return itemClient.update(ownerId, itemId, request);
    }
}
