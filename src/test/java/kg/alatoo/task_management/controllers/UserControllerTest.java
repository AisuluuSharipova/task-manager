package kg.alatoo.task_management.controllers;

import kg.alatoo.task_management.dtos.UserDTO;
import kg.alatoo.task_management.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() throws Exception {
        List<UserDTO> users = List.of(new UserDTO(1L, "Aisuluu", "Zhoodarbek", "aisuluu@example.com"));
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Aisuluu"));
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() throws Exception {
        UserDTO user = new UserDTO(1L, "Aisuluu", "Sharipova", "sharipova@example.com");
        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Aisuluu"));
    }

    @Test
    void getUserByEmail_ShouldReturnUser_WhenUserExists() throws Exception {
        UserDTO user = new UserDTO(1L, "Aisuluu", "Sharipova", "sharipova@example.com");
        when(userService.getUserByEmail("sharipova@example.com")).thenReturn(user);

        mockMvc.perform(get("/api/users/search/sharipova@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("sharipova@example.com"));
    }

    @Test
    void getUsersByName_ShouldReturnUsers_WhenUsersExist() throws Exception {
        List<UserDTO> users = List.of(new UserDTO(1L, "Aisuluu", "Sharipova", "sharipova@example.com"));
        when(userService.getUsersByName("Aisuluu", "Sharipova")).thenReturn(users);

        mockMvc.perform(get("/api/users/search")
                        .param("firstName", "Aisuluu")
                        .param("lastName", "Sharipova"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].lastName").value("Sharipova"));
    }

    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        UserDTO userDTO = new UserDTO(1L, "Aisuluu", "Zhoodarbek", "aisuluu@example.com");
        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"Aisuluu\", \"lastName\": \"Zhoodarbek\", \"email\": \"aisuluu@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("aisuluu@example.com"));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        UserDTO updatedUser = new UserDTO(1L, "Aisuluu", "Sharipova", "sharipova@example.com");
        when(userService.updateUser(eq(1L), any(UserDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"Aisuluu\", \"lastName\": \"Sharipova\", \"email\": \"sharipova@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("sharipova@example.com"));
    }

    @Test
    void deleteUser_ShouldReturnSuccessMessage() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully."));
    }
}
