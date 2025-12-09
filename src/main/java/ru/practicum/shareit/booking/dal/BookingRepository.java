package ru.practicum.shareit.booking.dal;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.status.BookingStatus;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    Booking save(Booking booking);

    Optional<Booking> findById(Long id);

    List<Booking> findAllBookingsForBooker(Long bookerId);

    List<Booking> findAllBookingsForOwner(Long ownerId);

    List<Booking> findCurrentBookingsForBooker(Long bookerId, BookingStatus status);

    List<Booking> findCurrentBookingsForOwner(Long ownerId, BookingStatus status);

    List<Booking> findPastBookingsForBooker(Long bookerId, BookingStatus status);

    List<Booking> findPastBookingsForOwner(Long ownerId, BookingStatus status);

    List<Booking> findFutureBookingsForBooker(Long bookerId, BookingStatus status);

    List<Booking> findFutureBookingsForOwner(Long ownerId, BookingStatus status);

    List<Booking> findWaitingBookingsForBooker(Long bookerId, BookingStatus status);

    List<Booking> findWaitingBookingsForOwner(Long ownerId, BookingStatus status);

    List<Booking> findRejectedBookingsForBooker(Long bookerId, BookingStatus status);

    List<Booking> findRejectedBookingsForOwner(Long ownerId, BookingStatus status);

    Optional<Booking> findLastBookingForItem(Long itemId, BookingStatus status);

    Optional<Booking> findNextBookingForItem(Long itemId, BookingStatus status);
}
