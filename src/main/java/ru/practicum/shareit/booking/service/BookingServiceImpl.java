package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.CreateBookingRequest;
import ru.practicum.shareit.booking.state.BookingState;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    public BookingDto create(Long bookerId, CreateBookingRequest request) {
        User user = userRepository.findById(bookerId)
                .orElseThrow(() ->
                        new NotFoundException("User with id " + bookerId + " was not found"));
        if (request.getStart().equals(request.getEnd())) {
            throw new ValidationException("Start time can not be equal end time");
        }
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() ->
                        new NotFoundException("Item with id " + request.getItemId() + " was not found"));
        if (!item.getAvailable()) {
            throw new ValidationException("Item is not allowed for booking");
        }
        Booking booking = BookingMapper.mapToBooking(request, item, user);
        booking = bookingRepository.save(booking);
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public BookingDto get(Long bookingId, Long userId) {
        Booking booking = getBooking(bookingId);
        if (booking.getBooker().getId().equals(userId) || booking.getItem().getOwner().getId().equals(userId)) {
            return BookingMapper.mapToBookingDto(booking);
        } else {
            throw new NotFoundException("You are not the owner either booker of this item");
        }
    }

    @Override
    public List<BookingDto> getBookingsForBooker(Long bookerId, BookingState state) {
        List<Booking> bookingList = new ArrayList<>();
        if (state == null || state.equals(BookingState.ALL)) {
            bookingList = bookingRepository.findAllBookingsForBooker(bookerId);
        } else if (state.equals(BookingState.CURRENT)) {
            bookingList = bookingRepository.findCurrentBookingsForBooker(bookerId, BookingStatus.APPROVED);
        } else if (state.equals(BookingState.PAST)) {
            bookingList = bookingRepository.findPastBookingsForBooker(bookerId, BookingStatus.APPROVED);
        } else if (state.equals(BookingState.FUTURE)) {
            bookingList = bookingRepository.findFutureBookingsForBooker(bookerId, BookingStatus.APPROVED);
        } else if (state.equals(BookingState.WAITING)) {
            bookingList = bookingRepository.findWaitingBookingsForBooker(bookerId, BookingStatus.WAITING);
        } else if (state.equals(BookingState.REJECTED)) {
            bookingList = bookingRepository.findRejectedBookingsForBooker(bookerId, BookingStatus.CANCELED);
        }
        return bookingList
                .stream()
                .map(BookingMapper::mapToBookingDto)
                .toList();
    }

    @Override
    public List<BookingDto> getBookingsForOwner(Long ownerId, BookingState state) {
        userRepository.findById(ownerId)
                .orElseThrow(() ->
                        new NotFoundException("User with id " + ownerId + " was not found"));
        List<Booking> bookingList = new ArrayList<>();
        if (state == null || state.equals(BookingState.ALL)) {
            bookingList = bookingRepository.findAllBookingsForOwner(ownerId);
        } else if (state.equals(BookingState.CURRENT)) {
            bookingList = bookingRepository.findCurrentBookingsForOwner(ownerId, BookingStatus.APPROVED);
        } else if (state.equals(BookingState.PAST)) {
            bookingList = bookingRepository.findPastBookingsForOwner(ownerId, BookingStatus.APPROVED);
        } else if (state.equals(BookingState.FUTURE)) {
            bookingList = bookingRepository.findFutureBookingsForOwner(ownerId, BookingStatus.APPROVED);
        } else if (state.equals(BookingState.WAITING)) {
            bookingList = bookingRepository.findWaitingBookingsForOwner(ownerId, BookingStatus.WAITING);
        } else if (state.equals(BookingState.REJECTED)) {
            bookingList = bookingRepository.findRejectedBookingsForOwner(ownerId, BookingStatus.CANCELED);
        }
        return bookingList
                .stream()
                .map(BookingMapper::mapToBookingDto)
                .toList();
    }

    @Override
    public BookingDto update(Long ownerId, Long bookingId, boolean approved) {
        Booking booking = getBooking(bookingId);
        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new ForbiddenException("You are not the owner of this item");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        booking = bookingRepository.save(booking);
        return BookingMapper.mapToBookingDto(booking);
    }

    private Booking getBooking(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Booking with id " + id + " was not found"));
    }
}
