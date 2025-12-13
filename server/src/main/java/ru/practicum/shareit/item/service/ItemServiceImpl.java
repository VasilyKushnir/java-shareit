package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dal.CommentRepository;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto create(Long id, CreateItemRequest request) {
        User owner = userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("User with id " + id + " was not found, so adding item is impossible"));
        Item item = ItemMapper.mapToItem(request);
        item.setOwner(owner);
        item = itemRepository.save(item);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, CreateCommentRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("User with id " + userId + " was not found"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() ->
                        new NotFoundException("Item with id " + itemId + " was not found"));
        List<Booking> bookingList = bookingRepository.findPastBookingsForBooker(userId, BookingStatus.APPROVED);
        boolean hasBookingForItem = bookingList
                .stream()
                .anyMatch(b -> b.getItem().getId().equals(itemId));
        if (!hasBookingForItem) {
            throw new ValidationException("User has no approved past bookings for this item");
        }
        Comment comment = CommentMapper.mapToComment(request, item, user);
        comment = commentRepository.save(comment);
        return CommentMapper.mapToCommentDto(comment);
    }

    @Override
    public ItemDtoExtended read(Long userId, Long itemId) {
        return itemRepository.findById(itemId)
                .map(i -> {
                    LocalDateTime lastBookingDate = null;
                    LocalDateTime nextBookingDate = null;
                    if (i.getOwner().getId().equals(userId)) {
                        Optional<Booking> lastBooking = bookingRepository
                                .findLastBookingForItem(itemId, BookingStatus.APPROVED);
                        if (lastBooking.isPresent()) {
                            lastBookingDate = lastBooking.get().getEnd();
                        }
                        Optional<Booking> nextBooking = bookingRepository
                                .findNextBookingForItem(itemId, BookingStatus.APPROVED);
                        if (nextBooking.isPresent()) {
                            nextBookingDate = nextBooking.get().getStart();
                        }
                    }

                    List<Comment> comments = commentRepository.findByItemId(itemId);

                    return ItemMapper.mapToItemDtoExtended(i, lastBookingDate, nextBookingDate, comments);
                })
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " does not exist"));
    }

    @Override
    public List<ItemDto> readUserItems(Long id) {
        return itemRepository.findByOwnerId(id).stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public List<ItemDto> searchItems(Long id, String text) {
        return itemRepository.searchItems(id, text).stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public ItemDto update(Long ownerId, Long itemId, UpdateItemRequest request) {
        Item currentItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " does not exist"));
        if (!currentItem.getOwner().getId().equals(ownerId)) {
            throw new ForbiddenException("User do not own item with id " + itemId);
        }
        Item updatedItem = ItemMapper.updateItem(currentItem, request);
        updatedItem = itemRepository.save(updatedItem);
        return ItemMapper.mapToItemDto(updatedItem);
    }
}
