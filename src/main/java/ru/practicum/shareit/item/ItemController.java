package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CreateItemRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
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
                          @RequestBody @Valid CreateItemRequest request) {
        return itemService.create(id, request);
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto read(@PathVariable("id") Long id) {
        return itemService.read(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> readUserItems(@RequestHeader("X-Sharer-User-Id") Long id) {
        return itemService.readUserItems(id);
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
