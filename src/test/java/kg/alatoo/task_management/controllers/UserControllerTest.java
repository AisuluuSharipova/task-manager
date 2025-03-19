package kg.alatoo.task_management.controllers;

import kg.alatoo.task_management.dtos.UserDTO;
import kg.alatoo.task_management.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

class UserControllerTest {

    private MockMvc mockMvc;
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        UserController userController = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void getAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(new UserDTO(1L, "Aisuluu", "Sharipova", "aisuluu@example.com")));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Aisuluu"));
    }

    @Test
    void getUserById() throws Exception {
        UserDTO userDTO = new UserDTO(1L, "Aisuluu", "Sharipova", "aisuluu@example.com");
        when(userService.getUserById(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("aisuluu@example.com"));
    }

    @Test
    void getUserByEmail() throws Exception {
        UserDTO userDTO = new UserDTO(1L, "Aisuluu", "Sharipova", "aisuluu@example.com");
        when(userService.getUserByEmail("aisuluu@example.com")).thenReturn(userDTO);

        mockMvc.perform(get("/api/users/search/aisuluu@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("aisuluu@example.com"));
    }

    @Test
    void getUsersByName() throws Exception {
        when(userService.getUsersByName("Aisuluu", "Sharipova"))
                .thenReturn(List.of(new UserDTO(1L, "Aisuluu", "Sharipova", "aisuluu@example.com")));

        mockMvc.perform(get("/api/users/search")
                        .param("firstName", "Aisuluu")
                        .param("lastName", "Sharipova"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Aisuluu"));
    }

    @Test
    void createUser() throws Exception {
        UserDTO userDTO = new UserDTO(1L, "Aisuluu", "Zhoodarbek", "aisuluu.zhoodarbek@example.com");
        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Aisuluu"));
    }

    @Test
    void updateUser() throws Exception {
        UserDTO updatedUser = new UserDTO(1L, "Aisuluu", "Sharipova", "updated@example.com");
        when(userService.updateUser(any(Long.class), any(UserDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    void partiallyUpdateUser() throws Exception {
        UserDTO patchedUser = new UserDTO(1L, "Updated", "Sharipova", "updated@example.com");
        when(userService.partiallyUpdateUser(any(Long.class), any(Map.class))).thenReturn(patchedUser);

        mockMvc.perform(patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("firstName", "Updated"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"));
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());
    }
}
