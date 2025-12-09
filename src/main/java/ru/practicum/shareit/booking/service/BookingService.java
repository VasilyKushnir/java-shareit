package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequest;
import ru.practicum.shareit.booking.state.BookingState;

import java.util.List;

public interface BookingService {
    BookingDto create(Long bookerId, CreateBookingRequest request);

    BookingDto get(Long bookingId, Long userId);

    List<BookingDto> getBookingsForBooker(Long bookerId, BookingState state);

    List<BookingDto> getBookingsForOwner(Long ownerId, BookingState state);

    BookingDto update(Long ownerId, Long bookingId, boolean approved);
}
