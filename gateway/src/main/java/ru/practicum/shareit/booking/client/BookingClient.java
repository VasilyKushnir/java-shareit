package ru.practicum.shareit.booking.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.CreateBookingRequest;
import ru.practicum.shareit.booking.state.BookingState;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> create(Long bookerId, CreateBookingRequest request) {
        return post("", bookerId, request);
    }

    public ResponseEntity<Object> get(Long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getBookingsForBooker(Long bookerId, BookingState state) {
        return get("?state={state}", bookerId, Map.of("state", state));
    }

    public ResponseEntity<Object> getBookingsForOwner(Long ownerId, BookingState state) {
        return get("owner?state={state}", ownerId, Map.of("state", state));
    }

    public ResponseEntity<Object> update(Long ownerId, Long bookingId, Boolean approved) {
        return patch("/" + bookingId + "?approved={approved}", ownerId, Map.of("approved", approved),
                null);
    }
}
