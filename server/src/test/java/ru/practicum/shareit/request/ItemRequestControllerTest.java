package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.CreateItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoExtended;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    private static final String USER_HEADER = "X-Sharer-User-Id";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createRequest_shouldReturnCreatedRequest() throws Exception {
        CreateItemRequest request =
                new CreateItemRequest("Need a drill");

        ItemRequestDto response = ItemRequestDto.builder()
                .id(1L)
                .description("Need a drill")
                .created(LocalDateTime.now())
                .build();

        Mockito.when(itemRequestService.create(Mockito.eq(1L), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/requests")
                        .header(USER_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Need a drill")));
    }

    @Test
    void getMyRequests_shouldReturnList() throws Exception {
        List<ItemRequestDtoExtended> response = List.of(
                ItemRequestDtoExtended.builder()
                        .id(1L)
                        .description("Need a drill")
                        .created(LocalDateTime.now())
                        .items(List.of())
                        .build()
        );

        Mockito.when(itemRequestService.getMyRequests(1L))
                .thenReturn(response);

        mockMvc.perform(get("/requests")
                        .header(USER_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].items", hasSize(0)));
    }

    @Test
    void getAllRequests_shouldReturnList() throws Exception {
        List<ItemRequestDto> response = List.of(
                ItemRequestDto.builder()
                        .id(1L)
                        .description("Need a drill")
                        .created(LocalDateTime.now())
                        .build()
        );

        Mockito.when(itemRequestService.getAllRequests())
                .thenReturn(response);

        mockMvc.perform(get("/requests/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void getSpecificRequest_shouldReturnRequest() throws Exception {
        ItemRequestDtoExtended response =
                ItemRequestDtoExtended.builder()
                        .id(1L)
                        .description("Need a drill")
                        .created(LocalDateTime.now())
                        .items(List.of())
                        .build();

        Mockito.when(itemRequestService.getSpecificRequest(1L))
                .thenReturn(response);

        mockMvc.perform(get("/requests/{requestId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Need a drill")))
                .andExpect(jsonPath("$.items", hasSize(0)));
    }
}
