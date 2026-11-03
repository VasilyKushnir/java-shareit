package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    private static final String USER_HEADER = "X-Sharer-User-Id";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createItem_shouldReturnCreatedItem() throws Exception {
        CreateItemRequest request =
                new CreateItemRequest("Drill", "Good drill", true, null);

        ItemDto response = ItemDto.builder()
                .id(1L)
                .name("Drill")
                .description("Good drill")
                .available(true)
                .build();

        Mockito.when(itemService.create(Mockito.eq(1L), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/items")
                        .header(USER_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Drill")))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    void addComment_shouldReturnCreatedComment() throws Exception {
        CreateCommentRequest request = new CreateCommentRequest("Nice item");

        CommentDto response =
                new CommentDto(1L, "Nice item", "John", null);

        Mockito.when(itemService.addComment(Mockito.eq(1L), Mockito.eq(1L), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header(USER_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.text", is("Nice item")))
                .andExpect(jsonPath("$.authorName", is("John")));
    }

    @Test
    void getItem_shouldReturnExtendedItem() throws Exception {
        ItemDtoExtended response =
                new ItemDtoExtended(1L, "Drill", "Good drill", true, null, null, List.of());

        Mockito.when(itemService.read(1L, 1L))
                .thenReturn(response);

        mockMvc.perform(get("/items/{itemId}", 1L)
                        .header(USER_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Drill")))
                .andExpect(jsonPath("$.comments", hasSize(0)));
    }

    @Test
    void getUserItems_shouldReturnList() throws Exception {
        List<ItemDto> response = List.of(
                ItemDto.builder()
                        .id(1L)
                        .name("Drill")
                        .description("Good drill")
                        .available(true)
                        .build()
        );

        Mockito.when(itemService.readUserItems(1L))
                .thenReturn(response);

        mockMvc.perform(get("/items")
                        .header(USER_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void searchItems_shouldReturnList() throws Exception {
        List<ItemDto> response = List.of(
                ItemDto.builder()
                        .id(1L)
                        .name("Drill")
                        .description("Good drill")
                        .available(true)
                        .build()
        );

        Mockito.when(itemService.searchItems(1L, "drill"))
                .thenReturn(response);

        mockMvc.perform(get("/items/search")
                        .header(USER_HEADER, 1L)
                        .param("text", "drill"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Drill")));
    }

    @Test
    void updateItem_shouldReturnUpdatedItem() throws Exception {
        UpdateItemRequest request =
                new UpdateItemRequest("New drill", null, null);

        ItemDto response = ItemDto.builder()
                .id(1L)
                .name("New drill")
                .description("Good drill")
                .available(true)
                .build();

        Mockito.when(itemService.update(Mockito.eq(1L), Mockito.eq(1L), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(patch("/items/{id}", 1L)
                        .header(USER_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New drill")));
    }
}
