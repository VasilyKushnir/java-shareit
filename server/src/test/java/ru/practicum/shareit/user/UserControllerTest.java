package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.CreateUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser_shouldReturnCreatedUser() throws Exception {
        CreateUserRequest request = new CreateUserRequest("John", "john@mail.com");
        UserDto response = new UserDto(1L, "John", "john@mail.com");

        Mockito.when(userService.create(Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John")))
                .andExpect(jsonPath("$.email", is("john@mail.com")));
    }

    @Test
    void getUser_shouldReturnUser() throws Exception {
        UserDto response = new UserDto(1L, "John", "john@mail.com");

        Mockito.when(userService.read(1L))
                .thenReturn(response);

        mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John")))
                .andExpect(jsonPath("$.email", is("john@mail.com")));
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest("Jack", null);
        UserDto response = new UserDto(1L, "Jack", "john@mail.com");

        Mockito.when(userService.update(Mockito.eq(1L), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(patch("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Jack")))
                .andExpect(jsonPath("$.email", is("john@mail.com")));
    }

    @Test
    void deleteUser_shouldReturnOk() throws Exception {
        Mockito.doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isOk());

        Mockito.verify(userService).delete(1L);
    }
}
