package ru.practicum.shareit.request.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.CreateItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoExtended;
import ru.practicum.shareit.user.dto.CreateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceImplTest {
    @Autowired
    ItemRequestService itemRequestService;

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    @Test
    void create_ShouldCreate() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@smaple.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest("itemRequestDescription");

        ItemRequestDto itemRequestDto = itemRequestService.create(userDto.getId(), createItemRequest);

        assertNotNull(itemRequestDto.getId());
    }

    @Test
    void create_ShouldThrowException_IfNoUser() {
        CreateItemRequest createItemRequest = new CreateItemRequest("itemRequestDescription");
        assertThrows(NotFoundException.class,
                () -> itemRequestService.create(684L, createItemRequest)
        );
    }

    @Test
    void getMyRequests_ShouldReturnList() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@smaple.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest("itemRequestDescription");

        itemRequestService.create(userDto.getId(), createItemRequest);

        List<ItemRequestDtoExtended> itemRequestDtoExtendedList = itemRequestService.getMyRequests(userDto.getId());

        assertEquals(1, itemRequestDtoExtendedList.size());
    }

    @Test
    void getAllRequests_ShouldReturnList() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@smaple.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest("itemRequestDescription");

        itemRequestService.create(userDto.getId(), createItemRequest);

        List<ItemRequestDto> itemRequestDtoList = itemRequestService.getAllRequests();

        assertEquals(1, itemRequestDtoList.size());
    }

    @Test
    void getSpecificRequest_ShouldReturn() {
        CreateUserRequest createUserRequest = new CreateUserRequest("John", "email@smaple.com");
        UserDto userDto = userService.create(createUserRequest);
        CreateItemRequest createItemRequest = new CreateItemRequest("itemRequestDescription");

        ItemRequestDto itemRequestDto = itemRequestService.create(userDto.getId(), createItemRequest);

        ItemRequestDtoExtended itemRequestDtoExtended = itemRequestService.getSpecificRequest(itemRequestDto.getId());

        assertNotNull(itemRequestDtoExtended.getId());
    }

    @Test
    void getSpecificRequest_ShouldThrownException_IfNoRequest() {
        assertThrows(NotFoundException.class,
                () -> itemRequestService.getSpecificRequest(777L));
    }
}
