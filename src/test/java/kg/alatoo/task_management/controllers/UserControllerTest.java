package kg.alatoo.task_management.controllers;

import kg.alatoo.task_management.dtos.UserDTO;
import kg.alatoo.task_management.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserDTO sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new UserDTO(1L, "Aisuluu", "Sharipova", "aisss@gmail.com");
    }

    @Test
    void getAllUsers() throws Exception {
        List<UserDTO> users = Arrays.asList(sampleUser);
        Mockito.when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Aisuluu"))
                .andExpect(jsonPath("$[0].lastName").value("Sharipova"))
                .andExpect(jsonPath("$[0].email").value("aisss@gmail.com"));
    }

    @Test
    void getUserById() throws Exception {
        Mockito.when(userService.getUserById(1L)).thenReturn(sampleUser);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Aisuluu"));
    }

    @Test
    void createUser() throws Exception {
        Mockito.when(userService.createUser(any(UserDTO.class))).thenReturn(sampleUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"Aisuluu\", \"lastName\": \"Sharipova\", \"email\": \"aisuluu@gmail.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Aisuluu"))
                .andExpect(jsonPath("$.lastName").value("Sharipova"));
    }

    @Test
    void updateUser() throws Exception {
        Mockito.when(userService.updateUser(eq(1L), any(UserDTO.class))).thenReturn(sampleUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"Updated\", \"lastName\": \"User\", \"email\": \"updated@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Aisuluu"));
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully."));
    }
}
