package ru.practicum.shareit.booking.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequest;
import ru.practicum.shareit.booking.state.BookingState;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CreateItemRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.CreateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImplTest {
    @Autowired
    BookingService bookingService;

    @Autowired
    UserService userService;

    @Autowired
    ItemService itemService;

    @Test
    void create_ShouldCreate() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@smaple.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);

        CreateUserRequest createUserRequest2 = new CreateUserRequest("Bob", "test@test.com");
        UserDto userDto2 = userService.create(createUserRequest2);
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                itemDto.getId(),
                LocalDateTime.now(), LocalDateTime.now().plusDays(2)
        );
        BookingDto bookingDto = bookingService.create(userDto2.getId(), createBookingRequest);

        assertNotNull(bookingDto.getId());
    }

    @Test
    void create_ShouldThrowException_IfNoUserExist() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@smaple.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);

        CreateUserRequest createUserRequest2 = new CreateUserRequest("Bob", "test@test.com");
        userService.create(createUserRequest2);
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                itemDto.getId(),
                LocalDateTime.now(), LocalDateTime.now().plusDays(2)
        );

        assertThrows(NotFoundException.class,
                () -> bookingService.create(149L, createBookingRequest));
    }

    @Test
    void create_ShouldThrowException_IfIncorrectTime() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@smaple.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);
        LocalDateTime date = LocalDateTime.now().plusHours(1);

        CreateUserRequest createUserRequest2 = new CreateUserRequest("Bob", "test@test.com");
        UserDto userDto2 = userService.create(createUserRequest2);
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                itemDto.getId(),
                date, date
        );

        assertThrows(ValidationException.class,
                () -> bookingService.create(userDto2.getId(), createBookingRequest));
    }

    @Test
    void create_ShouldThrowException_IfNoItem() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@smaple.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);

        CreateUserRequest createUserRequest2 = new CreateUserRequest("Bob", "test@test.com");
        UserDto userDto2 = userService.create(createUserRequest2);
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                234L,
                LocalDateTime.now(), LocalDateTime.now().plusDays(2)
        );

        assertThrows(NotFoundException.class,
                () -> bookingService.create(userDto2.getId(), createBookingRequest));
    }

    @Test
    void create_ShouldThrowException_IfItemNotAllowed() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@smaple.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", false, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);

        CreateUserRequest createUserRequest2 = new CreateUserRequest("Bob", "test@test.com");
        UserDto userDto2 = userService.create(createUserRequest2);
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                itemDto.getId(),
                LocalDateTime.now(), LocalDateTime.now().plusDays(2)
        );

        assertThrows(ValidationException.class,
                () -> bookingService.create(userDto2.getId(), createBookingRequest)
        );
    }

    @Test
    void get_ShouldReturn() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@smaple.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);

        CreateUserRequest createUserRequest2 = new CreateUserRequest("Bob", "test@test.com");
        UserDto userDto2 = userService.create(createUserRequest2);
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                itemDto.getId(),
                LocalDateTime.now(), LocalDateTime.now().plusDays(2)
        );
        BookingDto bookingDto = bookingService.create(userDto2.getId(), createBookingRequest);

        BookingDto bookingDto2 = bookingService.get(bookingDto.getId(), userDto2.getId());
        assertNotNull(bookingDto2.getId());
    }

    @Test
    void get_ShouldThrowException_IfWrongUser() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@smaple.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);

        CreateUserRequest createUserRequest2 = new CreateUserRequest("Bob", "test@test.com");
        UserDto userDto2 = userService.create(createUserRequest2);
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                itemDto.getId(),
                LocalDateTime.now(), LocalDateTime.now().plusDays(2)
        );
        BookingDto bookingDto = bookingService.create(userDto2.getId(), createBookingRequest);

        assertThrows(NotFoundException.class,
                () -> bookingService.get(bookingDto.getId(), 347L)
        );
    }

    @Test
    void getBookingsForBooker_ShouldReturnList_IfStateIsCurrent() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@smaple.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);

        CreateUserRequest createUserRequest2 = new CreateUserRequest("Bob", "test@test.com");
        UserDto userDto2 = userService.create(createUserRequest2);
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                itemDto.getId(),
                LocalDateTime.now(), LocalDateTime.now().plusDays(2)
        );
        BookingDto bookingDto = bookingService.create(userDto2.getId(), createBookingRequest);

        bookingService.update(userDto.getId(), bookingDto.getId(), true);

        List<BookingDto> bookingDtoList = bookingService.getBookingsForBooker(userDto2.getId(), BookingState.CURRENT);

        assertEquals(1, bookingDtoList.size());
    }

    @Test
    void getBookingsForBooker_ShouldReturnList_IfStateIsPast() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@smaple.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);

        CreateUserRequest createUserRequest2 = new CreateUserRequest("Bob", "test@test.com");
        UserDto userDto2 = userService.create(createUserRequest2);
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                itemDto.getId(),
                LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(2)
        );
        BookingDto bookingDto = bookingService.create(userDto2.getId(), createBookingRequest);

        bookingService.update(userDto.getId(), bookingDto.getId(), true);

        List<BookingDto> bookingDtoList = bookingService.getBookingsForBooker(userDto2.getId(), BookingState.PAST);

        assertEquals(1, bookingDtoList.size());
    }

    @Test
    void getBookingsForBooker_ShouldReturnList_IfStateIsFuture() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@smaple.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);

        CreateUserRequest createUserRequest2 = new CreateUserRequest("Bob", "test@test.com");
        UserDto userDto2 = userService.create(createUserRequest2);
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                itemDto.getId(),
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(3)
        );
        BookingDto bookingDto = bookingService.create(userDto2.getId(), createBookingRequest);

        bookingService.update(userDto.getId(), bookingDto.getId(), true);

        List<BookingDto> bookingDtoList = bookingService.getBookingsForBooker(userDto2.getId(), BookingState.FUTURE);

        assertEquals(1, bookingDtoList.size());
    }

    @Test
    void getBookingsForBooker_ShouldReturnList_IfStateIsWaiting() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@smaple.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);

        CreateUserRequest createUserRequest2 = new CreateUserRequest("Bob", "test@test.com");
        UserDto userDto2 = userService.create(createUserRequest2);
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                itemDto.getId(),
                LocalDateTime.now(), LocalDateTime.now().plusDays(2)
        );
        bookingService.create(userDto2.getId(), createBookingRequest);

        List<BookingDto> bookingDtoList = bookingService.getBookingsForBooker(userDto2.getId(), BookingState.WAITING);

        assertEquals(1, bookingDtoList.size());
    }

    @Test
    void getBookingsForBooker_ShouldReturnList_IfStateIsRejected() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@smaple.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);

        CreateUserRequest createUserRequest2 = new CreateUserRequest("Bob", "test@test.com");
        UserDto userDto2 = userService.create(createUserRequest2);
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                itemDto.getId(),
                LocalDateTime.now(), LocalDateTime.now().plusDays(2)
        );
        BookingDto bookingDto = bookingService.create(userDto2.getId(), createBookingRequest);

        bookingService.update(userDto.getId(), bookingDto.getId(), false);

        List<BookingDto> bookingDtoList = bookingService.getBookingsForBooker(userDto2.getId(), BookingState.REJECTED);

        assertEquals(1, bookingDtoList.size());
    }

    @Test
    void getBookingsForBooker_ShouldReturnList_IfStateIsDefaultOrNull() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@smaple.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);

        CreateUserRequest createUserRequest2 = new CreateUserRequest("Bob", "test@test.com");
        UserDto userDto2 = userService.create(createUserRequest2);
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                itemDto.getId(),
                LocalDateTime.now(), LocalDateTime.now().plusDays(2)
        );
        BookingDto bookingDto = bookingService.create(userDto2.getId(), createBookingRequest);

        bookingService.update(userDto.getId(), bookingDto.getId(), true);

        List<BookingDto> bookingDtoList = bookingService.getBookingsForBooker(userDto2.getId(), null);

        assertEquals(1, bookingDtoList.size());
    }

    @Test
    void getBookingsForOwner_ShouldReturnList_IfStateIsCurrent() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@smaple.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);

        CreateUserRequest createUserRequest2 = new CreateUserRequest("Bob", "test@test.com");
        UserDto userDto2 = userService.create(createUserRequest2);
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                itemDto.getId(),
                LocalDateTime.now(), LocalDateTime.now().plusDays(2)
        );
        BookingDto bookingDto = bookingService.create(userDto2.getId(), createBookingRequest);

        bookingService.update(userDto.getId(), bookingDto.getId(), true);

        List<BookingDto> bookingDtoList = bookingService.getBookingsForOwner(userDto.getId(), BookingState.CURRENT);

        assertEquals(1, bookingDtoList.size());
    }

    @Test
    void getBookingsForOwner_ShouldReturnList_IfStateIsPast() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@smaple.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);

        CreateUserRequest createUserRequest2 = new CreateUserRequest("Bob", "test@test.com");
        UserDto userDto2 = userService.create(createUserRequest2);
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                itemDto.getId(),
                LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(2)
        );
        BookingDto bookingDto = bookingService.create(userDto2.getId(), createBookingRequest);

        bookingService.update(userDto.getId(), bookingDto.getId(), true);

        List<BookingDto> bookingDtoList = bookingService.getBookingsForOwner(userDto.getId(), BookingState.PAST);

        assertEquals(1, bookingDtoList.size());
    }

    @Test
    void getBookingsForOwner_ShouldReturnList_IfStateIsFuture() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@smaple.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);

        CreateUserRequest createUserRequest2 = new CreateUserRequest("Bob", "test@test.com");
        UserDto userDto2 = userService.create(createUserRequest2);
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                itemDto.getId(),
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(3)
        );
        BookingDto bookingDto = bookingService.create(userDto2.getId(), createBookingRequest);

        bookingService.update(userDto.getId(), bookingDto.getId(), true);

        List<BookingDto> bookingDtoList = bookingService.getBookingsForOwner(userDto.getId(), BookingState.FUTURE);

        assertEquals(1, bookingDtoList.size());
    }

    @Test
    void getBookingsForOwner_ShouldReturnList_IfStateIsWaiting() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@smaple.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);

        CreateUserRequest createUserRequest2 = new CreateUserRequest("Bob", "test@test.com");
        UserDto userDto2 = userService.create(createUserRequest2);
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                itemDto.getId(),
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(3)
        );
        bookingService.create(userDto2.getId(), createBookingRequest);

        List<BookingDto> bookingDtoList = bookingService.getBookingsForOwner(userDto.getId(), BookingState.WAITING);

        assertEquals(1, bookingDtoList.size());
    }

    @Test
    void getBookingsForOwner_ShouldReturnList_IfStateIsRejected() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@smaple.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);

        CreateUserRequest createUserRequest2 = new CreateUserRequest("Bob", "test@test.com");
        UserDto userDto2 = userService.create(createUserRequest2);
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                itemDto.getId(),
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(3)
        );
        BookingDto bookingDto = bookingService.create(userDto2.getId(), createBookingRequest);

        bookingService.update(userDto.getId(), bookingDto.getId(), false);

        List<BookingDto> bookingDtoList = bookingService.getBookingsForOwner(userDto.getId(), BookingState.REJECTED);

        assertEquals(1, bookingDtoList.size());
    }

    @Test
    void getBookingsForOwner_ShouldReturnList_IfStateIsDefaultOrNull() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@smaple.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);

        CreateUserRequest createUserRequest2 = new CreateUserRequest("Bob", "test@test.com");
        UserDto userDto2 = userService.create(createUserRequest2);
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                itemDto.getId(),
                LocalDateTime.now(), LocalDateTime.now().plusDays(2)
        );
        BookingDto bookingDto = bookingService.create(userDto2.getId(), createBookingRequest);

        bookingService.update(userDto.getId(), bookingDto.getId(), true);

        List<BookingDto> bookingDtoList = bookingService.getBookingsForOwner(userDto.getId(), null);

        assertEquals(1, bookingDtoList.size());
    }

    @Test
    void getBookingsForOwner_ShouldThrowException_IfNoOwnerExist() {
        assertThrows(NotFoundException.class,
                () -> bookingService.getBookingsForOwner(485L, BookingState.CURRENT)
        );
    }

    @Test
    void update_ShouldThrowException_IfUserIsNotTheOwner() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@smaple.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest(
                "name", "description", true, null
        );
        ItemDto itemDto = itemService.create(userDto.getId(), createItemRequest);

        CreateUserRequest createUserRequest2 = new CreateUserRequest("Bob", "test@test.com");
        UserDto userDto2 = userService.create(createUserRequest2);
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                itemDto.getId(),
                LocalDateTime.now(), LocalDateTime.now().plusDays(2)
        );
        BookingDto bookingDto = bookingService.create(userDto2.getId(), createBookingRequest);

        assertThrows(ForbiddenException.class,
                () -> bookingService.update(557L, bookingDto.getId(), true)
        );
    }

    @Test
    void update_ShouldThrowException_IfNoBooking() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@smaple.com");
        UserDto userDto = userService.create(createUserRequest);

        assertThrows(NotFoundException.class,
                () -> bookingService.update(userDto.getId(), 613L, true)
        );
    }
}
