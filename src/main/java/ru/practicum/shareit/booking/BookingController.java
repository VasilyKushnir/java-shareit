package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequest;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.state.BookingState;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                             @RequestBody @Valid CreateBookingRequest request) {
        return bookingService.create(bookerId, request);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto get(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @PathVariable Long bookingId) {
        return bookingService.get(bookingId, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getBookingsForBooker(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                                 @RequestParam(required = false) BookingState state) {
        return bookingService.getBookingsForBooker(bookerId, state);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getBookingsForOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                          @RequestParam(required = false) BookingState state) {
        return bookingService.getBookingsForOwner(ownerId, state);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto update(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                             @PathVariable Long bookingId,
                             @RequestParam Boolean approved) {
        return bookingService.update(ownerId, bookingId, approved);
    }
}
