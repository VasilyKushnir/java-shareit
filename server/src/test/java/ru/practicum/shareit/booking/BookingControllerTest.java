package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequest;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.state.BookingState;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    private static final String USER_HEADER = "X-Sharer-User-Id";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createBooking_shouldReturnCreatedBooking() throws Exception {
        CreateBookingRequest request = new CreateBookingRequest(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        BookingDto response = BookingDto.builder()
                .id(1L)
                .build();

        Mockito.when(bookingService.create(Mockito.eq(1L), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/bookings")
                        .header(USER_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void getBooking_shouldReturnBooking() throws Exception {
        BookingDto response = BookingDto.builder()
                .id(1L)
                .build();

        Mockito.when(bookingService.get(1L, 1L))
                .thenReturn(response);

        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header(USER_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void getBookingsForBooker_shouldReturnList() throws Exception {
        List<BookingDto> response = List.of(
                BookingDto.builder().id(1L).build()
        );

        Mockito.when(bookingService.getBookingsForBooker(1L, BookingState.ALL))
                .thenReturn(response);

        mockMvc.perform(get("/bookings")
                        .header(USER_HEADER, 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void getBookingsForOwner_shouldReturnList() throws Exception {
        List<BookingDto> response = List.of(
                BookingDto.builder().id(1L).build()
        );

        Mockito.when(bookingService.getBookingsForOwner(1L, BookingState.ALL))
                .thenReturn(response);

        mockMvc.perform(get("/bookings/owner")
                        .header(USER_HEADER, 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void updateBooking_shouldReturnUpdatedBooking() throws Exception {
        BookingDto response = BookingDto.builder()
                .id(1L)
                .build();

        Mockito.when(bookingService.update(1L, 1L, true))
                .thenReturn(response);

        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header(USER_HEADER, 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }
}
