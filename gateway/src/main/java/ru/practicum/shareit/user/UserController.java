package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.CreateUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid CreateUserRequest request) {
        return userClient.create(request);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> read(@PathVariable("id") Long userId) {
        return userClient.read(userId);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long userId,
                                         @RequestBody @Valid UpdateUserRequest request) {
        return userClient.update(userId, request);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long userId) {
        return userClient.delete(userId);
    }
}
