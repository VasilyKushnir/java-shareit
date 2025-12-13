package ru.practicum.shareit.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.CreateUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImplTest {
    @Autowired
    UserService userService;

    CreateUserRequest request = new CreateUserRequest("John", "email@sample.com");

    @Test
    void create_ShouldCreate_IfNotExist() {
        UserDto userDto = userService.create(request);
        assertNotNull(userDto.getId());
    }

    @Test
    void create_ShouldThrowException_IfEmailOccupied() {
        CreateUserRequest request2 = new CreateUserRequest("Bob", "email@sample.com");
        UserDto userDto = userService.create(request);
        assertThrows(ConflictException.class, () ->
                userService.create(request2));
    }

    @Test
    void read_ShouldReturn_IfExist() {
        UserDto userDto = userService.create(request);
        UserDto userDto2 = userService.read(userDto.getId());
        assertNotNull(userDto2.getId());
    }

    @Test
    void read_ShouldThrowException_IfNotExist() {
        UserDto userDto = userService.create(request);
        assertThrows(NotFoundException.class, () ->
                userService.read(userDto.getId()+1));
    }

    @Test
    void update_ShouldUpdate_IfExist() {
        UserDto userDto = userService.create(request);
        UpdateUserRequest request2 = new UpdateUserRequest("Jannis", "email@sample.com");
        UserDto userDto2 = userService.update(userDto.getId(), request2);
        assertEquals("Jannis", userDto2.getName());
    }

    @Test
    void update_ShouldThrowException_IfEmailOccupied() {
        userService.create(request);
        CreateUserRequest request2 = new CreateUserRequest("Bob", "test@test.com");
        UserDto userDto = userService.create(request2);
        UpdateUserRequest updateRequest = new UpdateUserRequest("Bob", "email@sample.com");
        assertThrows(ConflictException.class, () ->
                userService.update(userDto.getId(), updateRequest));
    }

    @Test
    void delete_ShouldDelete() {
        UserDto userDto = userService.create(request);
        userService.delete(userDto.getId());
        assertThrows(NotFoundException.class, () ->
                userService.read(userDto.getId()));
    }
}
