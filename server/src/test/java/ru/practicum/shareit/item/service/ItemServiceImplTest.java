package ru.practicum.shareit.item.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequest;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.user.dto.CreateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplTest {
    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    @Autowired
    BookingService bookingService;

    @Test
    void create_ShouldCreate_IfUserExist() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@sample.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);
        assertNotNull(itemDto.getId());
    }

    @Test
    void create_ShouldThrowException_IfUserNotExist() {
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        assertThrows(NotFoundException.class, () ->
                itemService.create(1L, createItemRequest));
    }

    @Test
    void addComment_ShouldAdd() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@sample.com");
        UserDto userDto = userService.create(createUserRequest);

        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);

        CreateUserRequest createUserRequest2 = new CreateUserRequest("Bob", "test@test.com");
        UserDto userDto2 = userService.create(createUserRequest2);
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                itemDto.getId(),
                LocalDateTime.now().minus(Duration.ofDays(2)), LocalDateTime.now().minus(Duration.ofDays(1)));
        BookingDto bookingDto = bookingService.create(userDto2.getId(), createBookingRequest);
        bookingService.update(userDto.getId(),bookingDto.getId(), true);

        CreateCommentRequest createCommentRequest = new CreateCommentRequest("commentary text");
        CommentDto commentDto = itemService.addComment(userDto2.getId(), itemDto.getId(), createCommentRequest);
        assertNotNull(commentDto.getId());
    }

    @Test
    void addComment_ShouldThrowException_IfNoUser() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@sample.com");
        UserDto userDto = userService.create(createUserRequest);

        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);

        CreateUserRequest createUserRequest2 = new CreateUserRequest("Bob", "test@test.com");
        UserDto userDto2 = userService.create(createUserRequest2);
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                itemDto.getId(),
                LocalDateTime.now().minus(Duration.ofDays(2)), LocalDateTime.now().minus(Duration.ofDays(1)));
        BookingDto bookingDto = bookingService.create(userDto2.getId(), createBookingRequest);
        bookingService.update(userDto.getId(),bookingDto.getId(), true);

        CreateCommentRequest createCommentRequest = new CreateCommentRequest("commentary text");
        assertThrows(NotFoundException.class, () ->
                itemService.addComment(777L, itemDto.getId(), createCommentRequest));
    }

    @Test
    void addComment_ShouldThrowException_IfNoItem() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@sample.com");
        UserDto userDto = userService.create(createUserRequest);

        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);

        CreateUserRequest createUserRequest2 = new CreateUserRequest("Bob", "test@test.com");
        UserDto userDto2 = userService.create(createUserRequest2);
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                itemDto.getId(),
                LocalDateTime.now().minus(Duration.ofDays(2)), LocalDateTime.now().minus(Duration.ofDays(1)));
        BookingDto bookingDto = bookingService.create(userDto2.getId(), createBookingRequest);
        bookingService.update(userDto.getId(),bookingDto.getId(), true);

        CreateCommentRequest createCommentRequest = new CreateCommentRequest("commentary text");
        assertThrows(NotFoundException.class, () ->
                itemService.addComment(userDto2.getId(), 1061L, createCommentRequest));
    }

    @Test
    void addComment_ShouldThrowException_IfNoPastBookingForItem() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@sample.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);
        CreateCommentRequest createCommentRequest = new CreateCommentRequest("commentary text");
        assertThrows(ValidationException.class, () ->
                itemService.addComment(userDto.getId(), itemDto.getId(), createCommentRequest));
    }

    @Test
    void read_ShouldReturn_IfItemExist() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@sample.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);

        ItemDtoExtended itemDtoExtended = itemService.read(userDto.getId(), itemDto.getId());
        assertNotNull(itemDtoExtended.getId());
    }

    @Test
    void readUserItems_ShouldReturnList() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@sample.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);
        CreateItemRequest createItemRequest2 = new CreateItemRequest(
                "name2", "description2", true, null
        );
        ItemDto itemDto2 = itemService.create(userDto.getId(), createItemRequest);
        List<ItemDto> itemDtoList = itemService.readUserItems(userDto.getId());
        assertEquals(2, itemDtoList.size());
    }

    @Test
    void searchItems_ShouldReturnList() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@sample.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        CreateItemRequest createItemRequest2 = new CreateItemRequest(
                "name2", "description2", true, null
        );
        itemService.create(userDto.getId(), createItemRequest);
        itemService.create(userDto.getId(), createItemRequest2);
        List<ItemDto> itemDtoList = itemService.searchItems(userDto.getId(), "ion2");
        assertEquals(1, itemDtoList.size());
    }

    @Test
    void update_ShouldUpdate() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@sample.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);

        UpdateItemRequest updateItemRequest = new UpdateItemRequest(
                "updatedName", "updatedDescription", true
        );
        itemService.update(userDto.getId(), itemDto.getId(), updateItemRequest);
        List<ItemDto> itemDtoList = itemService.readUserItems(userDto.getId());

        assertEquals(1, itemDtoList.size());
        assertEquals("updatedName", itemDtoList.getFirst().getName());
    }
//
//    @Test
//    void update_ShouldNotUpdate_IfItemNotFound() {
//
//    }
//
//    @Test
//    void update_ShouldNotUpdate_IfUserDoNotOwnItem() {
//
//    }
}
