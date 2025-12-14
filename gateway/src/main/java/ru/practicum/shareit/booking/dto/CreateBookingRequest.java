package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class CreateBookingRequest {
    @NotNull(message = "Item id must not be blank")
    Long itemId;

    @FutureOrPresent(message = "Incorrect start time")
    LocalDateTime start;

    @Future(message = "Incorrect end time")
    LocalDateTime end;
}
