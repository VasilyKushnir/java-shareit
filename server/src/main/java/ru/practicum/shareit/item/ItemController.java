package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long id,
                          @RequestBody CreateItemRequest request) {
        return itemService.create(id, request);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable("itemId") Long itemId,
                                 @RequestBody CreateCommentRequest request) {
        return itemService.addComment(userId, itemId, request);
    }

    @GetMapping(path = "/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDtoExtended read(@RequestHeader("X-Sharer-User-Id") Long userId,
                                @PathVariable Long itemId) {
        return itemService.read(userId, itemId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> readUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.readUserItems(userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> searchItems(@RequestHeader("X-Sharer-User-Id") Long id,
                                     @RequestParam String text) {
        return itemService.searchItems(id, text);
    }

    @PatchMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                          @PathVariable("id") Long itemId,
                          @RequestBody UpdateItemRequest request) {
        return itemService.update(ownerId, itemId, request);
    }
}
